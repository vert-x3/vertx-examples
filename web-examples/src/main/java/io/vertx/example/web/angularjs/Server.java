package io.vertx.example.web.angularjs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.LinkedList;
import java.util.List;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  private MongoClient mongo;

  @Override
  public void start() throws Exception {

    // Create a mongo client using all defaults (connect to localhost and default port) using the database name "demo".
    mongo = MongoClient.createShared(vertx, new JsonObject().put("db_name", "demo"));

    // the load function just populates some data on the storage
    loadData(mongo);

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    // define some REST API
    router.get("/api/users").handler(ctx -> {
      mongo.find("users", new JsonObject(), lookup -> {
        // error handling
        if (lookup.failed()) {
          ctx.fail(500);
          return;
        }

        // now convert the list to a JsonArray because it will be easier to encode the final object as the response.
        final JsonArray json = new JsonArray();

        for (JsonObject o : lookup.result()) {
          json.add(o);
        }

        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        ctx.response().end(json.encode());
      });
    });

    router.get("/api/users/:id").handler(ctx -> {
      mongo.findOne("users", new JsonObject().put("_id", ctx.request().getParam("id")), null, lookup -> {
        // error handling
        if (lookup.failed()) {
          ctx.fail(500);
          return;
        }

        JsonObject user = lookup.result();

        if (user == null) {
          ctx.fail(404);
        } else {
          ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
          ctx.response().end(user.encode());
        }
      });
    });

    router.post("/api/users").handler(ctx -> {
      JsonObject newUser = ctx.getBodyAsJson();

      mongo.findOne("users", new JsonObject().put("username", newUser.getString("username")), null, lookup -> {
        // error handling
        if (lookup.failed()) {
          ctx.fail(500);
          return;
        }

        JsonObject user = lookup.result();

        if (user != null) {
          // already exists
          ctx.fail(500);
        } else {
          mongo.insert("users", newUser, insert -> {
            // error handling
            if (insert.failed()) {
              ctx.fail(500);
              return;
            }

            // add the generated id to the user object
            newUser.put("_id", insert.result());

            ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            ctx.response().end(newUser.encode());
          });
        }
      });
    });

    router.put("/api/users/:id").handler(ctx -> {
      mongo.findOne("users", new JsonObject().put("_id", ctx.request().getParam("id")), null, lookup -> {
        // error handling
        if (lookup.failed()) {
          ctx.fail(500);
          return;
        }

        JsonObject user = lookup.result();

        if (user == null) {
          // does not exist
          ctx.fail(404);
        } else {

          // update the user properties
          JsonObject update = ctx.getBodyAsJson();

          user.put("username", update.getString("username"));
          user.put("firstName", update.getString("firstName"));
          user.put("lastName", update.getString("lastName"));
          user.put("address", update.getString("address"));

          mongo.replace("users", new JsonObject().put("_id", ctx.request().getParam("id")), user, replace -> {
            // error handling
            if (replace.failed()) {
              ctx.fail(500);
              return;
            }

            ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            ctx.response().end(user.encode());
          });
        }
      });
    });

    router.delete("/api/users/:id").handler(ctx -> {
      mongo.findOne("users", new JsonObject().put("_id", ctx.request().getParam("id")), null, lookup -> {
        // error handling
        if (lookup.failed()) {
          ctx.fail(500);
          return;
        }

        JsonObject user = lookup.result();

        if (user == null) {
          // does not exist
          ctx.fail(404);
        } else {

          mongo.remove("users", new JsonObject().put("_id", ctx.request().getParam("id")), remove -> {
            // error handling
            if (remove.failed()) {
              ctx.fail(500);
              return;
            }

            ctx.response().setStatusCode(204);
            ctx.response().end();
          });
        }
      });
    });

    // Create a router endpoint for the static content.
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }

  private void loadData(MongoClient db) {
    db.dropCollection("users", drop -> {
      if (drop.failed()) {
        throw new RuntimeException(drop.cause());
      }

      List<JsonObject> users = new LinkedList<>();

      users.add(new JsonObject()
              .put("username", "pmlopes")
              .put("firstName", "Paulo")
              .put("lastName", "Lopes")
              .put("address", "The Netherlands"));

      users.add(new JsonObject()
              .put("username", "timfox")
              .put("firstName", "Tim")
              .put("lastName", "Fox")
              .put("address", "The Moon"));

      for (JsonObject user : users) {
        db.insert("users", user, res -> {
          System.out.println("inserted " + user.encode());
        });
      }
    });
  }
}

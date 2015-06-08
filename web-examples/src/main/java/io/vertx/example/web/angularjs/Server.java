package io.vertx.example.web.angularjs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Fake DB
  private static final Map<String, JsonObject> DB = new ConcurrentHashMap<>();

  static {

    insert(new JsonObject()
      .put("username", "pmlopes")
      .put("firstName", "Paulo")
      .put("lastName", "Lopes")
      .put("address", "The Netherlands"));

    insert(new JsonObject()
      .put("username", "timfox")
      .put("firstName", "Tim")
      .put("lastName", "Fox")
      .put("address", "The Moon"));
  }

  public static JsonArray findAll() {
    JsonArray l = new JsonArray();

    for (JsonObject u : DB.values()) {
      l.add(u);
    }

    return l;
  }

  public static JsonObject findById(String id) {
    return DB.get(id);
  }

  public static JsonObject findByUsername(String username) {
    for (JsonObject u : DB.values()) {
      if (u.getString("username", "").equals(username)) {
        return u;
      }
    }

    return null;
  }

  public static JsonObject insert(JsonObject u) {
    String id = UUID.randomUUID().toString();
    u.put("id", id);
    DB.put(id, u);
    return u;
  }

  public static JsonObject update(JsonObject u) {
    DB.put(u.getString("id"), u);
    return u;
  }

  public static void remove(String id) {
    DB.remove(id);
  }


  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    // define some REST API
    router.get("/api/users").handler(ctx -> {
      ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
      ctx.response().end(findAll().encode());
    });

    router.get("/api/users/:id").handler(ctx -> {
      JsonObject user = findById(ctx.request().getParam("id"));

      if (user == null) {
        ctx.fail(404);
      } else {
        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        ctx.response().end(user.encode());
      }
    });

    router.post("/api/users").handler(ctx -> {
      JsonObject user = findByUsername(ctx.getBodyAsJson().getString("username"));

      if (user != null) {
        // already exists
        ctx.fail(500);
      } else {
        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        user = insert(ctx.getBodyAsJson());

        ctx.response().end(user.encode());
      }
    });

    router.put("/api/users/:id").handler(ctx -> {
      JsonObject user = findById(ctx.request().getParam("id"));

      if (user == null) {
        ctx.fail(404);
      } else {
        user = update(user.mergeIn(ctx.getBodyAsJson()));

        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        ctx.response().end(user.encode());
      }
    });

    router.delete("/api/users/:id").handler(ctx -> {
      JsonObject user = findById(ctx.request().getParam("id"));

      if (user == null) {
        ctx.fail(404);
      } else {
        remove(ctx.request().getParam("id"));
        ctx.response().setStatusCode(204);
        ctx.response().end();
      }
    });

    // Create a router endpoint for the static content.
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}

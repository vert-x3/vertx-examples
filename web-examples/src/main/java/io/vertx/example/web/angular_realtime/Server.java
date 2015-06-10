package io.vertx.example.web.angular_realtime;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

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

    vertx.deployVerticle("maven:io.vertx:vertx-mongo-embedded-db:3.0.0-milestone6", done -> {
      // Create a mongo client using all defaults (connect to localhost and default port) using the database name "demo".
      mongo = MongoClient.createShared(vertx, new JsonObject().put("db_name", "demo"));

      // the load function just populates some data on the storage
      loadData(mongo);

      // the app works 100% realtime
      vertx.eventBus().consumer("vtoons.listAlbums", this::listAlbums);
      vertx.eventBus().consumer("vtoons.placeOrder", this::placeOrder);

      Router router = Router.router(vertx);

      // We need cookies and sessions
      router.route().handler(CookieHandler.create());
      router.route().handler(BodyHandler.create());
      router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

      // Simple auth service which uses a properties file for user/role info
      AuthProvider authProvider = ShiroAuth.create(vertx, ShiroAuthRealmType.PROPERTIES, new JsonObject());

      // We need a user session handler too to make sure the user is stored in the session between requests
      router.route().handler(UserSessionHandler.create(authProvider));

      router.post("/login").handler(ctx -> {
        JsonObject credentials = ctx.getBodyAsJson();
        if (credentials == null) {
          // bad request
          ctx.fail(400);
          return;
        }

        // use the auth handler to perform the authentication for us
        authProvider.authenticate(credentials, login -> {
          // error handling
          if (login.failed()) {
            // forbidden
            ctx.fail(403);
            return;
          }

          ctx.setUser(login.result());
          ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json").end("{}");
        });
      });

      router.route("/eventbus/*").handler(ctx -> {
        // we need to be logged in
        if (ctx.user() == null) {
          ctx.fail(403);
        } else {
          ctx.next();
        }
      });

      // Allow outbound traffic to the vtoons addresses
      BridgeOptions options = new BridgeOptions()
        // this is the public store allowed inbound
        .addInboundPermitted(new PermittedOptions()
          .setAddress("vtoons.listAlbums"))
          // this is the login inbound message
        .addInboundPermitted(new PermittedOptions()
          .setAddress("vtoons.login"))
          // this is the authenticated place orders inbound
        .addInboundPermitted(new PermittedOptions()
          .setAddress("vtoons.placeOrder")
          .setRequiredAuthority("place_order"))

          // all outbound messages are permitted
        .addOutboundPermitted(new PermittedOptions());

      router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options));

      // Serve the static resources
      router.route().handler(StaticHandler.create());

      vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    });
  }

  private void listAlbums(Message<JsonObject> msg) {
    // issue a find command to mongo to fetch all documents from the "albums" collection.
    mongo.find("albums", new JsonObject(), lookup -> {
      // error handling
      if (lookup.failed()) {
        msg.fail(500, lookup.cause().getMessage());
        return;
      }

      // now convert the list to a JsonArray because it will be easier to encode the final object as the response.
      final JsonArray json = new JsonArray();

      for (JsonObject o : lookup.result()) {
        json.add(o);
      }

      msg.reply(json);
    });

  }

  private void placeOrder(Message<JsonObject> msg) {
    mongo.save("orders", msg.body(), save -> {
      // error handling
      if (save.failed()) {
        msg.fail(500, save.cause().getMessage());
        return;
      }

      msg.reply(new JsonObject());
    });
  }

  private void loadData(MongoClient db) {
    db.dropCollection("albums", drop -> {
      if (drop.failed()) {
        throw new RuntimeException(drop.cause());
      }

      List<JsonObject> albums = new LinkedList<>();

      albums.add(new JsonObject()
        .put("artist", "The Wurzels")
        .put("genre", "Scrumpy and Western")
        .put("title", "I Am A Cider Drinker")
        .put("price", 0.99));

      albums.add(new JsonObject()
        .put("artist", "Vanilla Ice")
        .put("genre", "Hip Hop")
        .put("title", "Ice Ice Baby")
        .put("price", 0.01));

      albums.add(new JsonObject()
        .put("artist", "Ena Baga")
        .put("genre", "Easy Listening")
        .put("title", "The Happy Hammond")
        .put("price", 0.50));


      albums.add(new JsonObject()
        .put("artist", "The Tweets")
        .put("genre", "Bird related songs")
        .put("title", "The Birdy Song")
        .put("price", 1.20));

      for (JsonObject album : albums) {
        db.insert("albums", album, res -> {
          System.out.println("inserted " + album.encode());
        });
      }
    });
  }
}

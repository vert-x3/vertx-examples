package io.vertx.example.web.custom_authorisation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.List;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    // Create a JWT Auth Provider
    JWTAuth jwt = JWTAuth.create(vertx, new JsonObject()
        .put("keyStore", new JsonObject()
            .put("type", "jceks")
            .put("path", "keystore.jceks")
            .put("password", "secret")));

    // this route is excluded from the auth handler (it represents your login endpoint)
    router.get("/api/newToken").handler(ctx -> {
      List<String> authorities = ctx.request().params().getAll("authority");

      ctx.response().putHeader("Content-Type", "text/plain");

      if (authorities != null) {
        ctx.response().end(jwt.generateToken(new JsonObject(), new JWTOptions().setExpiresInSeconds(60).setPermissions(authorities)));
      } else {
        ctx.response().end(jwt.generateToken(new JsonObject(), new JWTOptions().setExpiresInSeconds(60)));
      }
    });

    router.route("/api/protected*").handler(JWTAuthHandler.create(jwt));

    router.get("/api/protected").handler(ctx -> {
      // protected the API (any authority is allowed)
      ctx.response().putHeader("Content-Type", "text/plain");
      ctx.response().end("this secret is not defcon!");
    });

    router.get("/api/protected/defcon1").handler(ctx -> {
      // protect the API (defcon1 authority is required)
      ctx.user().isAuthorised("defcon1", allowed -> {
        if (allowed.failed()) {
          ctx.fail(allowed.cause());
          return;
        }

        // user does not have the required authority
        if (!allowed.result()) {
          ctx.response().setStatusCode(403).end();
          return;
        }

        ctx.response().putHeader("Content-Type", "text/plain");
        ctx.response().end("this secret is defcon1!");
      });
    });

    router.get("/api/protected/defcon2").handler(ctx -> {
      // protect the API (defcon2 authority is required)
      ctx.user().isAuthorised("defcon2", allowed -> {
        if (allowed.failed()) {
          ctx.fail(allowed.cause());
          return;
        }

        // user does not have the required authority
        if (!allowed.result()) {
          ctx.response().setStatusCode(403).end();
          return;
        }

        ctx.response().putHeader("Content-Type", "text/plain");
        ctx.response().end("this secret is defcon2!");
      });
    });

    router.get("/api/protected/defcon3").handler(ctx -> {
      // protect the API (defcon3 authority is required)
      ctx.user().isAuthorised("defcon3", allowed -> {
        if (allowed.failed()) {
          ctx.fail(allowed.cause());
          return;
        }

        // user does not have the required authority
        if (!allowed.result()) {
          ctx.response().setStatusCode(403).end();
          return;
        }

        ctx.response().putHeader("Content-Type", "text/plain");
        ctx.response().end("this secret is defcon3!");
      });
    });

    // Serve the non private static pages
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}


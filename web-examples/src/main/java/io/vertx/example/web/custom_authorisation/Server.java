package io.vertx.example.web.custom_authorisation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.PermissionBasedAuthorization;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.jwt.authorization.JWTAuthorization;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ArrayList;
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
    JWTAuth jwt = JWTAuth.create(vertx, new JWTAuthOptions()
      .setKeyStore(new KeyStoreOptions()
        .setType("jceks")
        .setPath("keystore.jceks")
        .setPassword("secret")));

    // this route is excluded from the auth handler (it represents your login endpoint)
    router.get("/api/newToken").handler(ctx -> {
      List<String> authorities = new ArrayList<>();

      for (String authority : ctx.request().params().getAll("authority")) {
        authorities.add(authority);
      }

      ctx.response().putHeader("Content-Type", "text/plain");
      ctx.response().end(jwt.generateToken(new JsonObject(), new JWTOptions().setExpiresInSeconds(60).setPermissions
        (authorities)));
    });

    router.route("/api/protected*").handler(JWTAuthHandler.create(jwt));

    JWTAuthorization authzProvider = JWTAuthorization.create("permissions");

    router.get("/api/protected").handler(ctx -> {
      // protected the API (any authority is allowed)
      ctx.response().putHeader("Content-Type", "text/plain");
      ctx.response().end("this secret is not defcon!");
    });

    PermissionBasedAuthorization defcon1 = PermissionBasedAuthorization.create("defcon1");
    router.get("/api/protected/defcon1").handler(ctx -> {
      User user = ctx.user();
      authzProvider.getAuthorizations(user).onComplete(ar -> {
        if (ar.succeeded()) {
          // protect the API (defcon1 authority is required)
          if (defcon1.match(user)) {
            ctx.response().putHeader("Content-Type", "text/plain");
            ctx.response().end("this secret is defcon1!");
          } else {
            ctx.response().setStatusCode(403).end();
          }
        } else {
          ctx.fail(ar.cause());
        }
      });
    });

    PermissionBasedAuthorization defcon2 = PermissionBasedAuthorization.create("defcon2");
    router.get("/api/protected/defcon2").handler(ctx -> {
      User user = ctx.user();
      authzProvider.getAuthorizations(user).onComplete(ar -> {
        if (ar.succeeded()) {
          // protect the API (defcon2 authority is required)
          if (defcon2.match(user)) {
            ctx.response().putHeader("Content-Type", "text/plain");
            ctx.response().end("this secret is defcon2!");
          } else {
            ctx.response().setStatusCode(403).end();
          }
        } else {
          ctx.fail(ar.cause());
        }
      });
    });

    PermissionBasedAuthorization defcon3 = PermissionBasedAuthorization.create("defcon3");
    router.get("/api/protected/defcon3").handler(ctx -> {
      User user = ctx.user();
      authzProvider.getAuthorizations(user).onComplete(ar -> {
        if (ar.succeeded()) {
          // protect the API (defcon3 authority is required)
          if (defcon3.match(user)) {
            ctx.response().putHeader("Content-Type", "text/plain");
            ctx.response().end("this secret is defcon3!");
          } else {
            ctx.response().setStatusCode(403).end();
          }
        } else {
          ctx.fail(ar.cause());
        }
      });
    });

    // Serve the non private static pages
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }
}


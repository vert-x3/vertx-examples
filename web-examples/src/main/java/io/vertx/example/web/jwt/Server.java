package io.vertx.example.web.jwt;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.StaticHandler;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    // Create a JWT Auth Provider
    JWTAuth jwt = JWTAuth.create(vertx, new JWTAuthOptions()
      .setKeyStore(new KeyStoreOptions()
        .setType("jceks")
        .setPath("io/vertx/example/web/jwt/keystore.jceks")
        .setPassword("secret")));

    router.get("/api/newToken").handler(ctx -> {
      ctx.response().putHeader("Content-Type", "text/plain");
      ctx.response().end(jwt.generateToken(new JsonObject(), new JWTOptions().setExpiresInSeconds(60)));
    });

    // protect the API
    router.route("/api/*").handler(JWTAuthHandler.create(jwt));

    // this is the secret API
    router.get("/api/protected").handler(ctx -> {
      ctx.response().putHeader("Content-Type", "text/plain");
      ctx.response().end("a secret you should keep for yourself...");
    });

    // Serve the non-private static pages
    router.route().handler(StaticHandler.create("io/vertx/example/web/jwt/webroot"));

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }
}


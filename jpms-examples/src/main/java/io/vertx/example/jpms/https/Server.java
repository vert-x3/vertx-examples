package io.vertx.example.jpms.https;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerConfig;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.ServerSSLOptions;

public class Server extends VerticleBase {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Server())
      .onFailure(Throwable::printStackTrace);
  }

  @Override
  public Future<?> start() {
    HttpServer server = vertx
      .createHttpServer(
        new HttpServerConfig().setSsl(true),
        new ServerSSLOptions().setKeyCertOptions(new JksOptions().setPath("server-keystore.jks").setPassword("wibble")))
      .requestHandler(req -> {
        req.response().end(new JsonObject()
          .put("http", req.version())
          .put("message", "Hello World")
          .toString());
      });

    return server.listen(8443);
  }
}

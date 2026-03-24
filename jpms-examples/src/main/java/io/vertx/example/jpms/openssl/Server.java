package io.vertx.example.jpms.openssl;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerConfig;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.OpenSSLEngineOptions;
import io.vertx.core.net.ServerSSLOptions;

public class Server extends VerticleBase {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Server())
      .onFailure(Throwable::printStackTrace);
  }

  @Override
  public Future<?> start() {
    HttpServer server = vertx.httpServerBuilder()
      .with(new HttpServerConfig().setSsl(true))
      .with(new OpenSSLEngineOptions())
      .with(new ServerSSLOptions().setKeyCertOptions(new JksOptions()
        .setPath("server-keystore.jks")
        .setPassword("wibble")))
      .build()
      .requestHandler(req -> {
        req.response().end(new JsonObject()
          .put("http", req.version())
          .put("message", "Hello World")
          .put("nativeTransport", vertx.isNativeTransportEnabled())
          .toString());
      });

    return server.listen(8443);
  }
}

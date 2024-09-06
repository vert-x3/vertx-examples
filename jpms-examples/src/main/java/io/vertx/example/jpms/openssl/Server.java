package io.vertx.example.jpms.openssl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.OpenSSLEngineOptions;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Server())
      .onFailure(Throwable::printStackTrace);
  }

  @Override
  public void start(Promise<Void> startFuture) {
    HttpServer server = vertx.createHttpServer(new HttpServerOptions()
      .setSslEngineOptions(new OpenSSLEngineOptions())
      .setKeyCertOptions(new JksOptions()
        .setPath("server-keystore.jks")
        .setPassword("wibble"))
      .setSsl(true));
    server.requestHandler(req -> {
        req.response().end(new JsonObject()
          .put("http", req.version())
          .put("message", "Hello World")
          .put("nativeTransport", vertx.isNativeTransportEnabled())
          .toString());
      }).listen(8443)
      .<Void>mapEmpty()
      .onComplete(startFuture);
  }
}

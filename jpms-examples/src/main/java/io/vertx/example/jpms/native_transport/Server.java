package io.vertx.example.jpms.native_transport;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions()
      .setPreferNativeTransport(true));
    if (!vertx.isNativeTransportEnabled()) {
      throw new RuntimeException("Add your OS/arch specific modules (explained in README)");
    }
    vertx.deployVerticle(new Server())
      .onFailure(Throwable::printStackTrace);
  }

  @Override
  public void start(Promise<Void> startFuture) {
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
        req.response().end(new JsonObject()
          .put("http", req.version())
          .put("message", "Hello World")
          .put("nativeTransport", vertx.isNativeTransportEnabled())
          .toString());
      }).listen(8080)
      .<Void>mapEmpty()
      .onComplete(startFuture);
  }
}

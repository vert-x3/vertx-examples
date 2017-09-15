package io.vertx.example.unit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class HelloVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    int port = config().getInteger("http.port", 8080);
    vertx
      .createHttpServer()
      .requestHandler(request -> request.response().end("Hello!"))
      .listen(port, result -> startFuture.complete());
  }
}

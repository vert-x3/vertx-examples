package io.vertx.example.unit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class HelloVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    int port = config().getInteger("http.port", 8080);
    vertx
      .createHttpServer()
      .requestHandler(request -> request.response().end("Hello!"))
      .listen(port)
      .onComplete(result -> startPromise.complete());
  }
}

package io.vertx.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class HelloWorldVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    // Create an HTTP server which simply returns "Hello World!" to each request.
    vertx.createHttpServer().requestHandler(req -> req.response().end("Hello World!")).listen(8080);
  }
}


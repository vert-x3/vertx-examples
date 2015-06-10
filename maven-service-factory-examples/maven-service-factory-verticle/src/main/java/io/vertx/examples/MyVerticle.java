package io.vertx.examples;

import io.vertx.core.AbstractVerticle;

/**
 * A simple verticle.
 * The verticle is described in the `my-verticle.json` file.
 */
public class MyVerticle extends AbstractVerticle {

  @Override
  public void start() {
    // Create an HTTP server which simply returns "Hello World!" to each request.
    vertx.createHttpServer()
        .requestHandler(req -> req.response().end("Hello World!"))
        .listen(8080);
  }
}

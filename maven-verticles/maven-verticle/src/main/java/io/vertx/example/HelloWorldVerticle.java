package io.vertx.example;

import io.vertx.core.AbstractVerticle;

public class HelloWorldVerticle extends AbstractVerticle {

  @Override
  public void start() {
    // Create an HTTP server which simply returns "Hello World!" to each request.
    // If a configuration is set it get the specified name
    String name = config().getString("name", "World");
    vertx.createHttpServer().requestHandler(req -> req.response().end("Hello " + name + "!")).listen(8080);
  }
}


package io.vertx.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class HelloWorldVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.createHttpServer().requestHandler(req -> req.response().end("Hello World!"))
    .listen(
        Integer.getInteger("http.port"), System.getProperty("http.address"));
  }
}


package io.vertx.example;

import io.vertx.core.AbstractVerticle;

public class HelloWorldVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer().requestHandler(req -> req.response().end("Hello World!")).listen(8080);
    }

}

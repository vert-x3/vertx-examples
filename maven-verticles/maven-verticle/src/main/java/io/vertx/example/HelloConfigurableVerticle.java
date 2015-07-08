package io.vertx.example;

import io.vertx.core.AbstractVerticle;

/**
 * A verticle showing how the configuration can be retrieved.
 */
public class HelloConfigurableVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        String name = config().getString("name", "world");
        vertx.createHttpServer().requestHandler(req -> req.response().end("Hello " + name)).listen(8080);
    }

}

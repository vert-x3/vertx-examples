package io.vertx.example.openshift;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

import java.util.UUID;

public class IdGeneratorVerticle extends AbstractVerticle {

    @Override
    public void start() {
      Router router = Router.router(vertx);
      router.get("/").handler(rc -> rc.response().end(UUID.randomUUID().toString()));

      vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);
    }
}

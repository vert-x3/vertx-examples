package io.vertx.examples.openshift;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class MyHttpVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    router.get("/").handler(rc -> {
      String param = rc.request().getParam("name");
      if (param == null) {
        param = "world";
      }
      vertx.eventBus().<String>send("request", param, reply -> {
        if (reply.failed()) {
          rc.response().setStatusCode(400).end(reply.cause().getMessage());
        } else {
          String content = reply.result().body();
          rc.response().end(content);
        }
      });
    });

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);

  }
}

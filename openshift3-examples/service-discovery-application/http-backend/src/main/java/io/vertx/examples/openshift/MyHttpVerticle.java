package io.vertx.examples.openshift;

import io.vertx.core.AbstractVerticle;

public class MyHttpVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.createHttpServer()
        .requestHandler(request -> {
          String param = request.getParam("name");
          if (param == null) {
            param = "world";
          }

          request.response().end("Hello " + param);
        })
        .listen(8080);

  }
}

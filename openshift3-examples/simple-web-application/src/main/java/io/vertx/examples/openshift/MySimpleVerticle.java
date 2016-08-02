package io.vertx.examples.openshift;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class MySimpleVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    router.get("/").handler(rc -> {
      rc.response().end("Welcome");
    });
    router.get("/api").handler(rc -> {
      rc.response().end(new JsonObject().put("name", "my-awesome-api").put("version", 1).encode());
    });

    vertx.createHttpServer()
        .requestHandler(router::accept)
        .listen(8080);

  }
}

package io.vertx.example.kubernetes.greeting;


import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class GreetingApplication {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    Router router = Router.router(vertx);
    router.get("/").handler(rc -> rc.response().end("Hello"));
    router.get("/:name").handler(rc -> rc.response().end("Hello " + rc.pathParam("name")));

    vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(8080);
  }
}

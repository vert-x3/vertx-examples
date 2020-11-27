package io.vertx.example.jpms.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Server())
      .onFailure(Throwable::printStackTrace);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);

    router.route().handler(ctx -> {
      ctx.request().response().end("Hello World");
    });

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080)
      .<Void>mapEmpty()
      .onComplete(startPromise);
  }
}

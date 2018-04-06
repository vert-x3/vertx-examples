package io.vertx.example.micrometer.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

/**
 * @author Joel Takvorian, jtakvori@redhat.com
 */
public class SimpleWebServer extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    Router router = Router.router(vertx);
    router.get("/").handler(ctx -> {
      Greetings.get(vertx, greetingResult -> ctx.response().end(greetingResult.result()));
    });
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}

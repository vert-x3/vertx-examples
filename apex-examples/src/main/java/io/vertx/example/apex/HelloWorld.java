package io.vertx.example.apex;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.apex.Router;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class HelloWorld extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    router.route().handler(routingContext -> {
      routingContext.response().end("Hello World!");
    });

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}

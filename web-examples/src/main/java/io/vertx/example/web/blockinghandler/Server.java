package io.vertx.example.web.blockinghandler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.ext.web.Router;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    router.route().blockingHandler(routingContext -> {
      // Blocking handlers are allowed to block the calling thread
      // So let's simulate a blocking action or long running operation
      try {
        Thread.sleep(5000);
      } catch (Exception ignore) {
      }

      // Now call the next handler
      routingContext.next();
    }, false);

    router.route().handler(routingContext -> {
      routingContext.response().putHeader("content-type", "text/html").end("Hello World!");
    });

    vertx.createHttpServer().requestHandler(router).listen(8080);
  }
}

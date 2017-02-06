package io.vertx.example.webclient.send.stream;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    vertx.createHttpServer().requestHandler(req -> {

      req.bodyHandler(buff -> {
        System.out.println("Receiving user " + buff + " from client ");
        req.response().end();
      });

    }).listen(8080, listenResult -> {
      if (listenResult.failed()) {
        System.out.println("Could not start HTTP server");
        listenResult.cause().printStackTrace();
      } else {
        System.out.println("Server started");
      }
    });
  }
}

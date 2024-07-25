package io.vertx.example.webclient.send.jsonobject;

import io.vertx.core.AbstractVerticle;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public void start() throws Exception {

    vertx.createHttpServer().requestHandler(req -> {

      req.bodyHandler(buff -> {
        System.out.println("Receiving user " + buff.toJsonObject().encodePrettily() + " from client ");
        req.response().end();
      });

    }).listen(8080)
      .onComplete(listenResult -> {
      if (listenResult.failed()) {
        System.out.println("Could not start HTTP server");
        listenResult.cause().printStackTrace();
      } else {
        System.out.println("Server started");
      }
    });
  }
}

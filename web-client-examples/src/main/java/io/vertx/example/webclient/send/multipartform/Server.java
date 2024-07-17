package io.vertx.example.webclient.send.multipartform;

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
      System.out.println("Got form with content-type " + req.getHeader("content-type"));
      req.setExpectMultipart(true);
      req.endHandler(v -> {
        System.out.println("firstName: " + req.getFormAttribute("firstName"));
        System.out.println("lastName: " + req.getFormAttribute("lastName"));
        System.out.println("male: " + req.getFormAttribute("male"));

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

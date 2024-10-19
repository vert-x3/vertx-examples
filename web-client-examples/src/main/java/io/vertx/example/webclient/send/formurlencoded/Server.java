package io.vertx.example.webclient.send.formurlencoded;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() throws Exception {

    return vertx.createHttpServer().requestHandler(req -> {
      System.out.println("Got form with content-type " + req.getHeader("content-type"));
      req.setExpectMultipart(true);
      req.endHandler(v -> {
        System.out.println("firstName: " + req.getFormAttribute("firstName"));
        System.out.println("lastName: " + req.getFormAttribute("lastName"));
        System.out.println("male: " + req.getFormAttribute("male"));

        req.response().end();
      });

    }).listen(8080);
  }
}

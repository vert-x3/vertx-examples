package io.vertx.example.webclient.simple;

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

      req.response().end("Hello World");

    }).listen(8080);
  }
}

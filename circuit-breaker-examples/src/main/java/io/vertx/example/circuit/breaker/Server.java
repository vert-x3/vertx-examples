package io.vertx.example.circuit.breaker;

import io.vertx.core.AbstractVerticle;
import io.vertx.launcher.application.VertxApplication;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public void start() {
    vertx.createHttpServer()
    .requestHandler(req -> req.response().end("Bonjour"))
    .listen(8080);
  }

}

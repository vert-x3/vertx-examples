package io.vertx.example.circuit.breaker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() {
    vertx.createHttpServer()
    .requestHandler(req -> req.response().end("Bonjour"))
    .listen(8080);
  }

}

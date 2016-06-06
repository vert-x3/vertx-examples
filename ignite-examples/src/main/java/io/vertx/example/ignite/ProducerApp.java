package io.vertx.example.ignite;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class ProducerApp {

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions().setClustered(true), ar -> {
      if (ar.failed()) {
        System.err.println("Cannot create vert.x instance : " + ar.cause());
      } else {
        Vertx vertx = ar.result();
        vertx.setPeriodic(3000, x -> {
          System.out.println("Sending data to 'news'");
          vertx.eventBus().send("news", "hello vert.x");
        });
      }
    });
  }

}

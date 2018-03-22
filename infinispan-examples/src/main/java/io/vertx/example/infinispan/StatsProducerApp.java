package io.vertx.example.infinispan;

import java.util.concurrent.ThreadLocalRandom;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * @author Katia Aresti, karesti@redhat.com
 */
public class StatsProducerApp extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    System.out.println("StatsProducerApp start()");

    vertx.setPeriodic(2000, x -> {
      int stats = ThreadLocalRandom.current().nextInt(1, 10);
      System.out.println("Sending data to 'stats'");
      vertx.eventBus().send("stats", stats);
    });
  }

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions().setClustered(true), ar -> {
      if (ar.failed()) {
        System.err.println("Cannot create vert.x instance : " + ar.cause());
      } else {
        Vertx vertx = ar.result();
        vertx.deployVerticle(StatsProducerApp.class.getName());
      }
    });
  }

}

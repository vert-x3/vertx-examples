package io.vertx.example.infinispan;

import java.util.UUID;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.shareddata.Counter;

/**
 * @author Katia Aresti, karesti@redhat.com
 */
public class StatsConsumerApp extends AbstractVerticle {
  private String id = "ID-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

  @Override
  public void start() throws Exception {
    System.out.println(String.format("StatsConsumerApp [%s] start()", id));

    vertx.eventBus().<Integer>consumer("stats", message -> {
      Integer stats = message.body();
      System.out.println(">> received " + stats);
      vertx.sharedData().getCounter("globalStatsCounter", ar -> {
        if (ar.succeeded()) {
          Counter counter = ar.result();
          counter.addAndGet(stats.longValue(), opAr -> {
            if (opAr.succeeded()) {
              System.out.println("Stats added to global stats counter");
            } else {
              System.out.println(opAr.cause());
            }
          });
        } else {
          System.out.println(ar.cause());
        }
      });
    });
  }

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions().setClustered(true), ar -> {
      if (ar.failed()) {
        System.err.println("Cannot create vert.x instance : " + ar.cause());
      } else {
        Vertx vertx = ar.result();
        vertx.deployVerticle(StatsConsumerApp.class.getName());
      }
    });
  }
}

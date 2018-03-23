package io.vertx.example.infinispan;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.shareddata.Counter;

/**
 * @author Katia Aresti, karesti@redhat.com
 */
public class DisplayStatsApp extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    System.out.println("DisplayStatsApp start()");

    vertx.setPeriodic(2000, x ->
      vertx.sharedData().getCounter("globalStatsCounter", ar -> {
        if (ar.succeeded()) {
          Counter counter = ar.result();
          counter.get(stats -> System.out.println("Global stats counter value is " + stats.result()));
        } else {
          System.out.println(ar.cause());
        }
      }));
  }

  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions().setClustered(true), ar -> {
      if (ar.failed()) {
        System.err.println("Cannot create vert.x instance : " + ar.cause());
      } else {
        Vertx vertx = ar.result();
        vertx.deployVerticle(DisplayStatsApp.class.getName());
      }
    });
  }
}

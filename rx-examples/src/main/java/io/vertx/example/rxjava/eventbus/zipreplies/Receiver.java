package io.vertx.example.rxjava.eventbus.zipreplies;

import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;

import java.util.Random;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Receiver extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runClusteredExample(Receiver.class);
  }


  @Override
  public void start() throws Exception {

    Random random1 = new Random();
    EventBus eb = vertx.eventBus();

    eb.consumer("heatsensor1").
        toObservable().
        subscribe(message -> {
          message.reply(9 + random1.nextInt(5));
        });

    eb.consumer("heatsensor2").
        toObservable().
        subscribe(message -> {
          message.reply(10 + random1.nextInt(3));
        });
  }
}

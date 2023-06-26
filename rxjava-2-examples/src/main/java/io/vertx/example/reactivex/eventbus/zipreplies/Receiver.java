package io.vertx.example.reactivex.eventbus.zipreplies;

import io.vertx.core.Launcher;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;

import java.util.Random;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Receiver extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Receiver.class.getName(), "-cluster");
  }


  @Override
  public void start() throws Exception {

    Random random1 = new Random();
    EventBus eb = vertx.eventBus();

    eb.consumer("heatsensor1").
        toFlowable().
        subscribe(message -> {
          message.reply(9 + random1.nextInt(5));
        });

    eb.consumer("heatsensor2").
      toFlowable().
        subscribe(message -> {
          message.reply(10 + random1.nextInt(3));
        });
  }
}

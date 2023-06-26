package io.vertx.example.reactivex.eventbus.pubsub;

import io.vertx.core.Launcher;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Receiver extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Receiver.class.getName(), "-cluster");
  }


  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    eb.consumer("news-feed")
      .toFlowable()
      .subscribe(message -> System.out.println("Received news: " + message.body()));

    System.out.println("Ready!");
  }
}

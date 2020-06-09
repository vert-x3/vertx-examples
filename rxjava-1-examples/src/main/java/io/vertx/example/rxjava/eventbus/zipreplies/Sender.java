package io.vertx.example.rxjava.eventbus.zipreplies;

import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Single;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Sender extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runClusteredExample(Sender.class);
  }

  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    vertx.setPeriodic(1000, v -> {

      // Send two messages expecting replies
      Single<Message<Integer>> reply1 = eb.<Integer>rxRequest("heatsensor1", "ping");
      Single<Message<Integer>> reply2 = eb.<Integer>rxRequest("heatsensor2", "ping");

      // Zip responses to receive both at the same time
      Single<int[]> reply = reply1.zipWith(reply2, (msg1, msg2) -> new int[]{msg1.body(), msg2.body()});

      reply.subscribe(heats -> {

        // Print highest temp
        if (heats[0] > heats[1]) {
          System.out.println("heat sensor 1 is highest " + heats[0]);
        } else {
          System.out.println("heat sensor 2 is highest " + heats[1]);
        }

      }, err -> {

        System.out.println("Reply error:");
        err.printStackTrace();
      });
    });
  }
}

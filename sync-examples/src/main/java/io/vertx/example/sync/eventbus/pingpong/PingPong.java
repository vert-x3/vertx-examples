package io.vertx.example.sync.eventbus.pingpong;

import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.Strand;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.example.util.Runner;
import io.vertx.ext.sync.SyncVerticle;
import static io.vertx.ext.sync.Sync.*;

/**
 *
 * Example showing synchronous request / response of messages over the event bus
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class PingPong extends SyncVerticle {

  private static final String ADDRESS = "ping-address";

  /*
  Convenience method so you can run it in your IDE
  Note if in IDE need to edit run settings and add:
  -javaagent:/home/tim/.m2/repository/co/paralleluniverse/quasar-core/0.7.2/quasar-core-0.7.2.jar
   */
  public static void main(String[] args) {
    Runner.runExample(PingPong.class);
  }

  @Suspendable
  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    eb.consumer(ADDRESS).handler(msg -> msg.reply("pong"));

    // This runs on an event loop but the event loop is at no time blocked!
    for (int i = 0; i < 10; i++) {
      System.out.println("Thread is " + Thread.currentThread());
      Message<String> reply = syncResult(h -> eb.send(ADDRESS, "ping", h));
      System.out.println("got reply: " + reply.body());

      // Like Thread.sleep but doesn't block the OS thread
      Strand.sleep(1000);
    }

  }

}

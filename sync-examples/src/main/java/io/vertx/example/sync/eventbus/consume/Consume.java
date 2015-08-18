package io.vertx.example.sync.eventbus.consume;

import co.paralleluniverse.fibers.Suspendable;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.example.util.Runner;
import io.vertx.ext.sync.HandlerReceiverAdaptor;
import io.vertx.ext.sync.SyncVerticle;
import static io.vertx.ext.sync.Sync.*;


/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Consume extends SyncVerticle {

  private static final String ADDRESS1 = "address1";

  private static final String ADDRESS2 = "address2";

  /*
  Convenience method so you can run it in your IDE
  Note if in IDE need to edit run settings and add:
  -javaagent:/home/tim/.m2/repository/co/paralleluniverse/quasar-core/0.7.2/quasar-core-0.7.2.jar
   */
  public static void main(String[] args) {
    Runner.runExample(Consume.class);
  }

  @Suspendable
  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    // Create a couple of consumers on different addresses
    // The adaptor allows handler to be used as a Channel

    HandlerReceiverAdaptor<Message<String>> adaptor1 = streamAdaptor();
    eb.<String>consumer(ADDRESS1).handler(adaptor1);

    HandlerReceiverAdaptor<Message<String>> adaptor2 = streamAdaptor();
    eb.<String>consumer(ADDRESS2).handler(adaptor2);

    // Set up a periodic timer to send messages to these addresses

    vertx.setPeriodic(500, tid -> {
      eb.send(ADDRESS1, "wibble");
      eb.send(ADDRESS2, "flibble");
    });

    // This runs on an event loop but the event loop is at no time blocked!
    for (int i = 0; i < 10; i++) {
      System.out.println("Thread is " + Thread.currentThread());

      Message<String> received1 = adaptor1.receive();
      System.out.println("got message: " + received1.body());

      Message<String> received2 = adaptor2.receive();
      System.out.println("got message: " + received2.body());

    }

  }

}

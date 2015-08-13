package io.vertx.example.sync.eventbus.combine;

import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.channels.Mix;
import co.paralleluniverse.strands.channels.ReceivePort;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.example.util.Runner;
import io.vertx.ext.sync.HandlerReceiverAdaptor;
import io.vertx.ext.sync.SyncVerticle;

import static io.vertx.ext.sync.Sync.streamAdaptor;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Combine extends SyncVerticle {

  private static final String ADDRESS1 = "address1";

  private static final String ADDRESS2 = "address2";

  /*
  Convenience method so you can run it in your IDE
  Note if in IDE need to edit run settings and add:
  -javaagent:/home/tim/.m2/repository/co/paralleluniverse/quasar-core/0.7.2/quasar-core-0.7.2.jar
   */
  public static void main(String[] args) {
    Runner.runExample(Combine.class);
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

    ReceivePort<Message<String>> channel1 = adaptor1.receivePort();
    ReceivePort<Message<String>> channel2 = adaptor2.receivePort();

    // Combine them into a single channel

    // Not sure how to avoid this ugly cast with Quasar
    Mix<Message<String>> mix = (Mix<Message<String>>)Channels.mix(channel1, channel2);

    // Take the first ten
    for (int i = 0; i < 10; i++) {
      Message<String> msg = mix.receive();
      System.out.println("got message: " + msg.body());
    }

    System.out.println("done");
  }

}

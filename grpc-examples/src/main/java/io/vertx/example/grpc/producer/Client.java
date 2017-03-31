package io.vertx.example.grpc.producer;

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.ProducerServiceGrpc;
import io.vertx.example.util.Runner;
import io.vertx.grpc.VertxChannelBuilder;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    // Create the channel
    ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, "localhost", 8080)
      .usePlaintext(true)
      .build();

    // Get a stub to use for interacting with the remote service
    ProducerServiceGrpc.ProducerServiceVertxStub stub = ProducerServiceGrpc.newVertxStub(channel);

    // Call the remote service
    stub.streamingInputCall(exchange -> {
      exchange
        .handler(ar -> {
          if (ar.succeeded()) {
            System.out.println("Server replied OK");
          } else {
            ar.cause().printStackTrace();
          }
        });

      for (int i = 0; i < 10; i++) {
        exchange.write(Messages.StreamingInputCallRequest.newBuilder().build());
      }

      exchange.end();
    });
  }
}

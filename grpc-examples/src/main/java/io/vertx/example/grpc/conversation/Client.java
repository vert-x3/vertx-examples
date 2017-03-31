package io.vertx.example.grpc.conversation;

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.grpc.ConversationalServiceGrpc;
import io.vertx.example.grpc.Messages;
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
    ConversationalServiceGrpc.ConversationalServiceVertxStub stub = ConversationalServiceGrpc.newVertxStub(channel);

    // Make a request
    Messages.StreamingOutputCallRequest request = Messages.StreamingOutputCallRequest.newBuilder().build();

    // Call the remote service
    stub.fullDuplexCall(exchange -> {
      exchange
        .handler(req -> {
          System.out.println("Client: received response");
          vertx.setTimer(500L, t -> {
            exchange.write(Messages.StreamingOutputCallRequest.newBuilder().build());
          });
        });

      // start the conversation
      exchange.write(Messages.StreamingOutputCallRequest.newBuilder().build());
    });
  }
}

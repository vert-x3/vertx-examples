package io.vertx.example.grpc.conversation;

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxConversationalServiceGrpc;
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
  public void start() {

    // Create the channel
    ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, "localhost", 8080)
      .usePlaintext()
      .build();

    // Get a stub to use for interacting with the remote service
    VertxConversationalServiceGrpc.ConversationalServiceVertxStub stub = VertxConversationalServiceGrpc.newVertxStub(channel);

    // Make a request
    Messages.StreamingOutputCallRequest request = Messages.StreamingOutputCallRequest.newBuilder().build();

    // Call the remote service
    stub.fullDuplexCall(writeStream -> {
      // start the conversation
      writeStream.write(Messages.StreamingOutputCallRequest.newBuilder().build());
      vertx.setTimer(500L, t -> {
        writeStream.write(Messages.StreamingOutputCallRequest.newBuilder().build());
      });
    }).handler(req -> {
      System.out.println("Client: received response");
    });
  }
}

package io.vertx.example.grpc.consumer;

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxConsumerServiceGrpc;
import io.vertx.example.util.Runner;
import io.vertx.grpc.VertxChannelBuilder;

import java.nio.charset.StandardCharsets;

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
      .usePlaintext(true)
      .build();

    // Get a stub to use for interacting with the remote service
    VertxConsumerServiceGrpc.ConsumerServiceVertxStub stub = VertxConsumerServiceGrpc.newVertxStub(channel);

    // Make a request
    Messages.StreamingOutputCallRequest request = Messages
      .StreamingOutputCallRequest
      .newBuilder()
      .build();

    // Call the remote service
    stub.streamingOutputCall(request).handler(response -> {
      System.out.println(new String(response.getPayload().toByteArray(), StandardCharsets.UTF_8));
    }).endHandler(v -> {
      System.out.println("Response has ended.");
    });
  }
}

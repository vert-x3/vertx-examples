package io.vertx.example.grpc.empty;

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.grpc.EmptyPingPongServiceGrpc;
import io.vertx.example.grpc.EmptyProtos;
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
    EmptyPingPongServiceGrpc.EmptyPingPongServiceVertxStub stub = EmptyPingPongServiceGrpc.newVertxStub(channel);

    // Make a request
    EmptyProtos.Empty request = EmptyProtos.Empty.newBuilder().build();

    // Call the remote service
    stub.emptyCall(request, ar -> {
      if (ar.succeeded()) {
        System.out.println("Got the server response.");
      } else {
        System.out.println("Coult not reach server " + ar.cause().getMessage());
      }
    });
  }
}

package io.vertx.example.grpc.pingpong;

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.PingPongServiceGrpc;
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
    PingPongServiceGrpc.PingPongServiceVertxStub stub = PingPongServiceGrpc.newVertxStub(channel);

    // Make a request
    Messages.SimpleRequest request = Messages.SimpleRequest.newBuilder().setFillUsername(true).build();

    // Call the remote service
    stub.unaryCall(request, ar -> {
      if (ar.succeeded()) {
        System.out.println("My username is: " + ar.result().getUsername());
      } else {
        System.out.println("Coult not reach server " + ar.cause().getMessage());
      }
    });
  }
}

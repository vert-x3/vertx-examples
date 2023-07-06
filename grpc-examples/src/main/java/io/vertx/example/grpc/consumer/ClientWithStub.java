package io.vertx.example.grpc.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxConsumerServiceGrpc;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.client.GrpcClientChannel;

import java.nio.charset.StandardCharsets;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ClientWithStub extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", ClientWithStub.class.getName());
  }

  @Override
  public void start() {

    // Create the channel
    GrpcClient client = GrpcClient.client(vertx);
    GrpcClientChannel channel = new GrpcClientChannel(client, SocketAddress.inetSocketAddress(8080, "localhost"));

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

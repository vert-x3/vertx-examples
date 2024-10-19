package io.vertx.example.grpc.empty;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.EmptyProtos;
import io.vertx.example.grpc.VertxEmptyPingPongServiceGrpcClient;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ClientWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientWithStub.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {

    // Create the channel
    client = GrpcClient.client(vertx);

    // Get a stub to use for interacting with the remote service
    VertxEmptyPingPongServiceGrpcClient stub = new VertxEmptyPingPongServiceGrpcClient(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    // Make a request
    EmptyProtos.Empty request = EmptyProtos.Empty.newBuilder().build();

    // Call the remote service
    return stub
      .emptyCall(request)
      .onSuccess(empty -> {
        System.out.println("Got the server response.");
      });
  }
}

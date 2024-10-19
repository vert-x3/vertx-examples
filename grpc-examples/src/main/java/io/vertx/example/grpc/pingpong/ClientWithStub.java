package io.vertx.example.grpc.pingpong;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxPingPongServiceGrpcClient;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ClientWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientWithStub.class.getName()});
  }

  @Override
  public Future<?> start() {

    // Create the channel
    GrpcClient client = GrpcClient.client(vertx);

    // Get a stub to use for interacting with the remote service
    VertxPingPongServiceGrpcClient stub = new VertxPingPongServiceGrpcClient(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    // Make a request
    Messages.SimpleRequest request = Messages.SimpleRequest.newBuilder().setFillUsername(true).build();

    // Call the remote service
    return stub
      .unaryCall(request)
      .onSuccess(res -> System.out.println("My username is: " + res.getUsername()));
  }
}

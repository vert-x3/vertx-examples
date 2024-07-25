package io.vertx.example.grpc.empty;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.EmptyProtos;
import io.vertx.example.grpc.VertxEmptyPingPongServiceGrpcClient;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.GrpcReadStream;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public void start() {

    // Create the channel
    GrpcClient client = GrpcClient.client(vertx);

    // Call the remote service
    client.request(SocketAddress.inetSocketAddress(8080, "localhost"), VertxEmptyPingPongServiceGrpcClient.EmptyCall)
      .compose(request -> {
        request.end(EmptyProtos.Empty.newBuilder().build());
        return request.response().compose(GrpcReadStream::last);
      }).onSuccess(empty -> {
        System.out.println("Got the server response.");
      }).onFailure(cause -> {
        System.out.println("Could not reach server " + cause.getMessage());
      });
  }
}

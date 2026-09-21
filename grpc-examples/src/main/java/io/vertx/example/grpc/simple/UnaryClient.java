package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.GrpcReadStream;
import io.vertx.launcher.application.VertxApplication;

public class UnaryClient extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{UnaryClient.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {
    client = GrpcClient.client(vertx);

    return client.request(SocketAddress.inetSocketAddress(8080, "localhost"), ExampleServiceGrpcClient.Unary)
      .compose(request -> {
        request.end(Request.newBuilder().setValue("World").build());
        return request.response().compose(GrpcReadStream::last);
      })
      .onSuccess(response -> System.out.println("Received: " + response.getValue()));
  }
}

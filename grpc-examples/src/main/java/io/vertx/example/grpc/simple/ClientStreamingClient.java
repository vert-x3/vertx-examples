package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.launcher.application.VertxApplication;

public class ClientStreamingClient extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientStreamingClient.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {
    client = GrpcClient.client(vertx);

    return client.request(SocketAddress.inetSocketAddress(8080, "localhost"), ExampleServiceGrpcClient.ClientStreaming)
      .compose(request -> {
        for (int i = 0; i < 10; i++) {
          request.write(Request.newBuilder().setValue("Item #" + i).build());
        }
        request.end();
        return request.response();
      })
      .onSuccess(response -> System.out.println("Server replied OK"));
  }
}

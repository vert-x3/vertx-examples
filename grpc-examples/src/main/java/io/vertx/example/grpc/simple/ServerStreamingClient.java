package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.launcher.application.VertxApplication;

public class ServerStreamingClient extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerStreamingClient.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {
    client = GrpcClient.client(vertx);

    return client.request(SocketAddress.inetSocketAddress(8080, "localhost"), ExampleServiceGrpcClient.ServerStreaming)
      .compose(request -> {
        request.end(Request.newBuilder().setValue("stream request").build());
        return request.response().compose(response -> {
          response.handler(msg -> System.out.println("Received: " + msg.getValue()));
          return response.end();
        });
      })
      .onSuccess(v -> System.out.println("Stream ended."));
  }
}

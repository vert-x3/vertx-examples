package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.launcher.application.VertxApplication;

public class BidiStreamingClient extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{BidiStreamingClient.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {
    client = GrpcClient.client(vertx);

    return client.request(SocketAddress.inetSocketAddress(8080, "localhost"), ExampleServiceGrpcClient.BidiStreaming)
      .compose(request -> {
        request.write(Request.newBuilder().setValue("ping").build());
        vertx.setTimer(500L, t -> {
          request.write(Request.newBuilder().setValue("ping").build());
        });
        return request.response();
      })
      .compose(response -> {
        response.handler(msg -> System.out.println("Client received: " + msg.getValue()));
        return response.end();
      });
  }
}

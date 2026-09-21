package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.client.GrpcClientResponse;
import io.vertx.launcher.application.VertxApplication;

public class ServerStreamingClientWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerStreamingClientWithStub.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {
    client = GrpcClient.client(vertx);

    ExampleServiceGrpcClient stub = ExampleServiceGrpcClient.create(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    Request request = Request.newBuilder().setValue("stream request").build();

    return stub
      .serverStreaming(request)
      .compose(response -> {
        response.handler(msg -> System.out.println("Received: " + msg.getValue()));
        return ((GrpcClientResponse)response).last().map("Stream ended.");
      });
  }
}

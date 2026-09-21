package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.launcher.application.VertxApplication;

public class BidiStreamingClientWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{BidiStreamingClientWithStub.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {
    client = GrpcClient.client(vertx);

    ExampleServiceGrpcClient stub = ExampleServiceGrpcClient.create(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    return stub.bidiStreaming((writeStream, err) -> {
      writeStream.write(Request.newBuilder().setValue("ping").build());
      vertx.setTimer(500L, t -> {
        writeStream.write(Request.newBuilder().setValue("ping").build());
      });
    }).onSuccess(response -> {
      response.handler(msg -> System.out.println("Client received: " + msg.getValue()));
    });
  }
}

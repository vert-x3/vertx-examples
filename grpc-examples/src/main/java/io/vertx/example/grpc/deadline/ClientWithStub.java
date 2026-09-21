package io.vertx.example.grpc.deadline;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.client.GrpcClientOptions;
import io.vertx.launcher.application.VertxApplication;

import java.util.concurrent.TimeUnit;

public class ClientWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientWithStub.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {
    client = GrpcClient.client(vertx, new GrpcClientOptions()
      .setTimeout(5)
      .setTimeoutUnit(TimeUnit.SECONDS));
    ExampleServiceGrpcClient stub = ExampleServiceGrpcClient.create(client, SocketAddress.inetSocketAddress(8080, "localhost"));
    Request request = Request.newBuilder().setValue("Julien").build();
    System.out.println("Sending a request that should timeout due to the server deadline");
    return stub
      .unary(request)
      .map(Response::getValue)
      .recover(err -> {
        System.out.println("Timeout as expected");
        return Future.succeededFuture("Expected timeout");
      });
  }
}

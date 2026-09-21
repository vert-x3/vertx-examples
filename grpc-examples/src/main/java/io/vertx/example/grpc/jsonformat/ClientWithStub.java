package io.vertx.example.grpc.jsonformat;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.WireFormat;
import io.vertx.launcher.application.VertxApplication;

public class ClientWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientWithStub.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {
    client = GrpcClient.client(vertx);
    ExampleServiceGrpcClient stub = ExampleServiceGrpcClient.create(client, SocketAddress.inetSocketAddress(8080, "localhost"), WireFormat.JSON);
    Request request = Request.newBuilder().setValue("Julien").build();
    return stub
      .unary(request)
      .onSuccess(res -> System.out.println("Succeeded " + res.getValue()));
  }
}

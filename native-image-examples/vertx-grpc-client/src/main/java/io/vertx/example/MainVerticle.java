package io.vertx.example;

import io.grpc.ManagedChannel;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.grpc.VertxChannelBuilder;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, "localhost", 50051)
      .usePlaintext(true)
      .build();
    GreeterGrpc.GreeterVertxStub stub = GreeterGrpc.newVertxStub(channel);
    HelloRequest request = HelloRequest.newBuilder().setName("Paulo").build();
    stub.sayHello(request, asyncResponse -> {
      if (asyncResponse.succeeded()) {
        System.out.println("Succeeded " + asyncResponse.result().getMessage());
      } else {
        asyncResponse.cause().printStackTrace();
      }
    });
  }
}

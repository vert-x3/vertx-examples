package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientStreamingServer extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientStreamingServer.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {
    GrpcServer rpcServer = GrpcServer.server(vertx);

    rpcServer.callHandler(ExampleServiceGrpcService.ClientStreaming, request -> {
      AtomicInteger count = new AtomicInteger();
      request.handler(msg -> {
        System.out.println("Received: " + msg.getValue());
        count.incrementAndGet();
      }).endHandler(v -> {
        System.out.println("Client stream ended.");
        request.response().end(Response.newBuilder()
          .setValue("Received " + count.get() + " items")
          .build());
      });
    });

    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

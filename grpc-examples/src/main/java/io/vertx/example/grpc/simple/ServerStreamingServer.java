package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerStreamingServer extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerStreamingServer.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {
    GrpcServer rpcServer = GrpcServer.server(vertx);

    rpcServer.callHandler(ExampleServiceGrpcService.ServerStreaming, request -> {
      AtomicInteger counter = new AtomicInteger();
      vertx.setPeriodic(1000L, t -> {
        request.response().write(Response.newBuilder()
          .setValue("Item #" + counter.incrementAndGet())
          .build());
      });
    });

    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

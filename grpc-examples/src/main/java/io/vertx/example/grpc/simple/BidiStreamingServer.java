package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

public class BidiStreamingServer extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{BidiStreamingServer.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {
    GrpcServer rpcServer = GrpcServer.server(vertx);

    rpcServer.callHandler(ExampleServiceGrpcService.BidiStreaming, request -> {
      request.handler(msg -> {
        System.out.println("Server received: " + msg.getValue());
        vertx.setTimer(500L, t -> {
          request.response().write(Response.newBuilder()
            .setValue("Echo: " + msg.getValue())
            .build());
        });
      });
    });

    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

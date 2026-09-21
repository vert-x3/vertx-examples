package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

public class UnaryServer extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{UnaryServer.class.getName()});
    System.out.println("Server started");
  }

  private final int port;

  public UnaryServer(int port) {
    this.port = port;
  }

  public UnaryServer() {
    this(8080);
  }

  @Override
  public Future<?> start() {
    GrpcServer rpcServer = GrpcServer.server(vertx);

    rpcServer.callHandler(ExampleServiceGrpcService.Unary, request -> {
      request
        .last()
        .onSuccess(msg -> {
          System.out.println("Hello " + msg.getValue());
          request.response().end(Response.newBuilder().setValue("Hello " + msg.getValue()).build());
        });
    });

    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(port);
  }
}

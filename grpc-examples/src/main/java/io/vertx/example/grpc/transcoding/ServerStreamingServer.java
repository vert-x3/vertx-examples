package io.vertx.example.grpc.transcoding;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

public class ServerStreamingServer extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerStreamingServer.class.getName()});
    System.out.println("Server started");
  }

  private final int port;

  public ServerStreamingServer(int port) {
    this.port = port;
  }

  public ServerStreamingServer() {
    this(8080);
  }

  @Override
  public Future<?> start() {
    GrpcServer rpcServer = GrpcServer.server(vertx);

    rpcServer.callHandler(ExampleServiceGrpcService.ServerStreaming, request -> {
      request
        .last()
        .onSuccess(msg -> {
          System.out.println("Hello " + msg.getValue());
          for (int i = 0; i < 5; i++) {
            request.response().write(Response.newBuilder().setValue(msg.getValue() + " " + i).build());
          }
          request.response().end();
        });
    });

    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(port);
  }
}

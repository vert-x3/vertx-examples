package io.vertx.example.grpc.transcoding;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Request;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

public class UnaryServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{UnaryServerWithStub.class.getName()});
    System.out.println("Server started");
  }

  private final int port;

  public UnaryServerWithStub(int port) {
    this.port = port;
  }

  public UnaryServerWithStub() {
    this(8080);
  }

  @Override
  public Future<?> start() {
    ExampleServiceGrpcService service = new ExampleServiceGrpcService() {
      @Override
      public Future<Response> unary(Request request) {
        System.out.println("Hello " + request.getValue());
        return Future.succeededFuture(Response.newBuilder().setValue(request.getValue()).build());
      }
    };

    GrpcServer rpcServer = GrpcServer.server(vertx);
    rpcServer.addService(service);

    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(port);
  }
}

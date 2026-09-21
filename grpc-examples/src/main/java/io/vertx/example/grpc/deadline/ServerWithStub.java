package io.vertx.example.grpc.deadline;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Request;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.grpc.server.GrpcServerOptions;
import io.vertx.launcher.application.VertxApplication;

public class ServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerWithStub.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {
    ExampleServiceGrpcService service = new ExampleServiceGrpcService() {
      @Override
      public Future<Response> unary(Request request) {
        // Do not send a response to trigger timeout
        return Promise.<Response>promise().future();
      }
    };

    GrpcServer rpcServer = GrpcServer.server(vertx, new GrpcServerOptions().setScheduleDeadlineAutomatically(true));
    rpcServer.addService(service);

    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

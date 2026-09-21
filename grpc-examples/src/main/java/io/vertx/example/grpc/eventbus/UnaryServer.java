package io.vertx.example.grpc.eventbus;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Request;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.eventbus.EventBusGrpcServer;
import io.vertx.launcher.application.VertxApplication;

public class UnaryServer extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{UnaryServer.class.getName(), "-cluster"});
  }

  @Override
  public Future<?> start() throws Exception {
    ExampleServiceGrpcService service = new ExampleServiceGrpcService() {
      @Override
      public Future<Response> unary(Request request) {
        System.out.println("Hello " + request.getValue());
        return Future.succeededFuture(Response.newBuilder().setValue(request.getValue()).build());
      }
    };

    return EventBusGrpcServer
      .server(vertx)
      .onSuccess(rpcServer -> {
        rpcServer.addService(service);
    });
  }
}

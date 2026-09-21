package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Request;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerStreamingServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerStreamingServerWithStub.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {
    ExampleServiceGrpcService service = new ExampleServiceGrpcService() {
      @Override
      protected void serverStreaming(Request request, WriteStream<Response> response) {
        AtomicInteger counter = new AtomicInteger();
        vertx.setPeriodic(1000L, t -> {
          response.write(Response.newBuilder()
            .setValue("Item #" + counter.incrementAndGet())
            .build());
        });
      }
    };

    GrpcServer rpcServer = GrpcServer.server(vertx);
    rpcServer.addService(service);

    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

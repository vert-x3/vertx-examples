package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.VerticleBase;
import io.vertx.core.streams.ReadStream;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Request;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientStreamingServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientStreamingServerWithStub.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {
    ExampleServiceGrpcService service = new ExampleServiceGrpcService() {
      @Override
      public Future<Response> clientStreaming(ReadStream<Request> request) {
        Promise<Response> promise = Promise.promise();
        AtomicInteger count = new AtomicInteger();
        request.handler(msg -> {
          System.out.println("Received: " + msg.getValue());
          count.incrementAndGet();
        }).endHandler(v -> {
          System.out.println("Client stream ended.");
          promise.complete(Response.newBuilder()
            .setValue("Received " + count.get() + " items")
            .build());
        });
        return promise.future();
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

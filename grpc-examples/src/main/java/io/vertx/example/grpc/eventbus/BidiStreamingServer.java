package io.vertx.example.grpc.eventbus;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Request;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.eventbus.EventBusGrpcServer;
import io.vertx.launcher.application.VertxApplication;

public class BidiStreamingServer extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{BidiStreamingServer.class.getName(), "-cluster"});
  }

  @Override
  public Future<?> start() throws Exception {

    ExampleServiceGrpcService service = new ExampleServiceGrpcService() {
      @Override
      protected void bidiStreaming(ReadStream<Request> request, WriteStream<Response> response) {
        request
          .handler(req -> {
            System.out.println("Server: received request");
            vertx.setTimer(500L, t -> {
              response.write(Response.newBuilder().setValue("Echo: " + req.getValue()).build());
            });
          });
      }
    };

    return EventBusGrpcServer
      .server(vertx)
      .onSuccess(rpcServer -> {
        rpcServer.addService(service);
    });
  }
}

package io.vertx.example.grpc.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Request;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

public class BidiStreamingServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{BidiStreamingServerWithStub.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {
    ExampleServiceGrpcService service = new ExampleServiceGrpcService() {
      @Override
      protected void bidiStreaming(ReadStream<Request> request, WriteStream<Response> response) {
        request.handler(msg -> {
          System.out.println("Server received: " + msg.getValue());
          vertx.setTimer(500L, t -> {
            response.write(Response.newBuilder()
              .setValue("Echo: " + msg.getValue())
              .build());
          });
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

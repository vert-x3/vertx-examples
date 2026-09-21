package io.vertx.example.grpc.transcoding;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Request;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

public class ServerStreamingServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerStreamingServerWithStub.class.getName()});
    System.out.println("Server started");
  }

  private final int port;

  public ServerStreamingServerWithStub(int port) {
    this.port = port;
  }

  public ServerStreamingServerWithStub() {
    this(8080);
  }

  @Override
  public Future<?> start() {
    ExampleServiceGrpcService service = new ExampleServiceGrpcService() {
      @Override
      protected void serverStreaming(Request request, WriteStream<Response> response) {
        System.out.println("Hello " + request.getValue());
        for (int i = 0; i < 5; i++) {
          response.write(Response.newBuilder().setValue(request.getValue() + " " + i).build());
        }
        response.end();
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

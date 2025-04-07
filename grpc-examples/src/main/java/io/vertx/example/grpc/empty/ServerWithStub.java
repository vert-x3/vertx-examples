package io.vertx.example.grpc.empty;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.EmptyPingPongServiceGrpcService;
import io.vertx.example.grpc.EmptyProtos;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerWithStub.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {


    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    // The rpc service
    EmptyPingPongServiceGrpcService service = new EmptyPingPongServiceGrpcService() {
      @Override
      public Future<EmptyProtos.Empty> emptyCall(EmptyProtos.Empty request) {
        return Future.succeededFuture(EmptyProtos.Empty.newBuilder().build());
      }
    };

    // Bind the service
    rpcServer.addService(service);

    // start the server
    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

package io.vertx.example.grpc.empty;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.EmptyProtos;
import io.vertx.example.grpc.VertxEmptyPingPongServiceGrpcServer;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    // The rpc service
    rpcServer.callHandler(VertxEmptyPingPongServiceGrpcServer.EmptyCall, request -> {
      request.response().end(EmptyProtos.Empty.newBuilder().build());
    });

    // start the server
    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

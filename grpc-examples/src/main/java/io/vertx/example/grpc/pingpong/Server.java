package io.vertx.example.grpc.pingpong;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxPingPongServiceGrpcServer;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public Future<?> start() {

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    //
    rpcServer.callHandler(VertxPingPongServiceGrpcServer.UnaryCall, request -> {
      request
        .last()
        .onSuccess(msg -> {
          request.response().end(Messages.SimpleResponse.newBuilder().setUsername("Paulo").build());
        });
    });

    // start the server
    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

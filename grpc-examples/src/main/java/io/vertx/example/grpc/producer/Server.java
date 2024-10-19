package io.vertx.example.grpc.producer;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxProducerServiceGrpcServer;
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

    // The rpc service
    rpcServer.callHandler(VertxProducerServiceGrpcServer.StreamingInputCall, request -> {
      request.handler(payload -> {
        System.out.println(payload.getPayload().getType().getNumber());
      }).endHandler(v -> {
        System.out.println("Request has ended.");
        request.response().end(Messages.StreamingInputCallResponse.newBuilder().build());
      });
    });

    // start the server
    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

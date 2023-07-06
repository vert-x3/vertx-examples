package io.vertx.example.grpc.pingpong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxPingPongServiceGrpc;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.grpc.server.GrpcServiceBridge;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() {

    // The rpc service
    VertxPingPongServiceGrpc.PingPongServiceVertxImplBase service = new VertxPingPongServiceGrpc.PingPongServiceVertxImplBase() {
      @Override
      public Future<Messages.SimpleResponse> unaryCall(Messages.SimpleRequest request) {
        return Future.succeededFuture(Messages.SimpleResponse.newBuilder().setUsername("Paulo").build());
      }
    };

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);
    GrpcServiceBridge
      .bridge(service)
      .bind(rpcServer);

    // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure(cause -> {
        cause.printStackTrace();
      });
  }
}

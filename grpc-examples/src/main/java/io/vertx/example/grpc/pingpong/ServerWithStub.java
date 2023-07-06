package io.vertx.example.grpc.pingpong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.PingPongServiceGrpc;
import io.vertx.example.grpc.VertxPingPongServiceGrpc;
import io.vertx.grpc.server.GrpcServer;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ServerWithStub extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", ServerWithStub.class.getName());
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

    // The rpc service
    rpcServer.callHandler(PingPongServiceGrpc.getUnaryCallMethod(), request -> {
      request
        .last()
        .onSuccess(msg -> {
          request.response().end(Messages.SimpleResponse.newBuilder().setUsername("Paulo").build());
        });
    });

    // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure(cause -> {
        cause.printStackTrace();
      });
  }
}

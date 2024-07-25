package io.vertx.example.grpc.pingpong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.PingPongServiceGrpc;
import io.vertx.grpc.server.GrpcServer;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() {

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    //
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

package io.vertx.example.grpc.conversation;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.example.grpc.ConversationalServiceGrpc;
import io.vertx.example.grpc.Messages;
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

    // The rpc service
    rpcServer.callHandler(ConversationalServiceGrpc.getFullDuplexCallMethod(), request -> {
      System.out.println("Server: received request");
      vertx.setTimer(500L, t -> {
        request.response().write(Messages.StreamingOutputCallResponse.newBuilder().build());
      });
    });

    // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure(cause -> {
        cause.printStackTrace();
      });
  }
}

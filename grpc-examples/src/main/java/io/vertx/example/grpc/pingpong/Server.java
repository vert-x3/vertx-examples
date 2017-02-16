package io.vertx.example.grpc.pingpong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.example.grpc.EmptyPingPongServiceGrpc;
import io.vertx.example.grpc.EmptyProtos;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.PingPongServiceGrpc;
import io.vertx.example.util.Runner;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    // The rcp service
    PingPongServiceGrpc.PingPongServiceVertxImplBase service = new PingPongServiceGrpc.PingPongServiceVertxImplBase() {
      @Override
      public void unaryCall(Messages.SimpleRequest request, Future<Messages.SimpleResponse> future) {
        future.complete(Messages.SimpleResponse.newBuilder().setUsername("Paulo").build());
      }
    };

    // Create the server
    VertxServer rpcServer = VertxServerBuilder
      .forPort(vertx, 8080)
      .addService(service)
      .build();

    // start the server
    rpcServer.start(ar -> {
      if (ar.failed()) {
        ar.cause().printStackTrace();
      }
    });
  }
}

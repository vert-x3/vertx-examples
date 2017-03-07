package io.vertx.example.grpc.producer;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.ProducerServiceGrpc;
import io.vertx.example.util.Runner;
import io.vertx.grpc.GrpcReadStream;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

import java.util.concurrent.atomic.AtomicInteger;

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
    ProducerServiceGrpc.ProducerServiceVertxImplBase service = new ProducerServiceGrpc.ProducerServiceVertxImplBase() {
      @Override
      public void streamingInputCall(GrpcReadStream<Messages.StreamingInputCallRequest> request, Future<Messages.StreamingInputCallResponse> future) {
        request.handler(payload -> {
          System.out.println(payload.getPayload().getType().getNumber());
        }).endHandler(v -> {
          System.out.println("Request has ended.");
          future.complete(Messages.StreamingInputCallResponse.newBuilder().build());
        });
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

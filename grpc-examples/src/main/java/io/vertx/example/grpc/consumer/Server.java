package io.vertx.example.grpc.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.grpc.*;
import io.vertx.example.util.Runner;
import io.vertx.grpc.GrpcWriteStream;
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
    ConsumerServiceGrpc.ConsumerServiceVertxImplBase service = new ConsumerServiceGrpc.ConsumerServiceVertxImplBase() {
      @Override
      public void streamingOutputCall(Messages.StreamingOutputCallRequest request, GrpcWriteStream<Messages.StreamingOutputCallResponse> response) {
        final AtomicInteger counter = new AtomicInteger();
        vertx.setPeriodic(1000L, t -> {
          response.write(Messages.StreamingOutputCallResponse.newBuilder().setPayload(Messages.Payload.newBuilder().setTypeValue(counter.incrementAndGet())).build());
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

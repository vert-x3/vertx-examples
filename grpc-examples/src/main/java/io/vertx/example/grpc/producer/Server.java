package io.vertx.example.grpc.producer;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.streams.ReadStream;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxProducerServiceGrpc;
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
  public void start() {

    // The rpc service
    VertxProducerServiceGrpc.ProducerServiceImplBase service = new VertxProducerServiceGrpc.ProducerServiceImplBase() {
      @Override
      public Future<Messages.StreamingInputCallResponse> streamingInputCall(ReadStream<Messages.StreamingInputCallRequest> request) {
        Promise<Messages.StreamingInputCallResponse> promise = Promise.promise();
        request.handler(payload -> {
          System.out.println(payload.getPayload().getType().getNumber());
        }).endHandler(v -> {
          System.out.println("Request has ended.");
          promise.complete(Messages.StreamingInputCallResponse.newBuilder().build());
        });
        return promise.future();
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

package io.vertx.example.grpc.producer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.streams.ReadStream;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxProducerServiceGrpc;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.grpc.server.GrpcServiceBridge;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ServerWithStub extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerWithStub.class.getName()});
  }

  @Override
  public void start() {

    // The rpc service
    VertxProducerServiceGrpc.ProducerServiceVertxImplBase service = new VertxProducerServiceGrpc.ProducerServiceVertxImplBase() {
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

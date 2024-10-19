package io.vertx.example.grpc.producer;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxProducerServiceGrpcServer;
import io.vertx.grpc.common.GrpcReadStream;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerWithStub.class.getName()});
  }

  @Override
  public Future<?> start() {

    // The rpc service
    VertxProducerServiceGrpcServer.ProducerServiceApi service = new VertxProducerServiceGrpcServer.ProducerServiceApi() {
      @Override
      public Future<Messages.StreamingInputCallResponse> streamingInputCall(GrpcReadStream<Messages.StreamingInputCallRequest> request) {
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

    // Bind the service
    service.bind_streamingInputCall(rpcServer);

    // start the server
    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

package io.vertx.example.grpc.conversation;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxConversationalServiceGrpcServer;
import io.vertx.grpc.common.GrpcReadStream;
import io.vertx.grpc.common.GrpcWriteStream;
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
    VertxConversationalServiceGrpcServer.ConversationalServiceApi service = new VertxConversationalServiceGrpcServer.ConversationalServiceApi() {
      @Override
      public void fullDuplexCall(GrpcReadStream<Messages.StreamingOutputCallRequest> request, GrpcWriteStream<Messages.StreamingOutputCallResponse> response) {
        request
          .handler(req -> {
            System.out.println("Server: received request");
            vertx.setTimer(500L, t -> {
              response.write(Messages.StreamingOutputCallResponse.newBuilder().build());
            });
          });
      }
    };

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    // Bind the service
    service.bindAll(rpcServer);

    // start the server
    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

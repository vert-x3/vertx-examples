package io.vertx.example.grpc.conversation;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.grpc.ConversationalServiceGrpcService;
import io.vertx.example.grpc.Messages;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerWithStub.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {

    // The rpc service
    ConversationalServiceGrpcService service = new ConversationalServiceGrpcService() {
      @Override
      protected void fullDuplexCall(ReadStream<Messages.StreamingOutputCallRequest> request, WriteStream<Messages.StreamingOutputCallResponse> response) {
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
    rpcServer.addService(service);

    // start the server
    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

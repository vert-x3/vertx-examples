package io.vertx.example.grpc.conversation;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxConversationalServiceGrpc;
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
    VertxConversationalServiceGrpc.ConversationalServiceImplBase service = new VertxConversationalServiceGrpc.ConversationalServiceImplBase() {
      @Override
      public void fullDuplexCall(ReadStream<Messages.StreamingOutputCallRequest> request, WriteStream<Messages.StreamingOutputCallResponse> response) {
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

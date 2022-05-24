package io.vertx.example.grpc.producer;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.ProducerServiceGrpc;
import io.vertx.example.util.Runner;
import io.vertx.grpc.server.GrpcServer;

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

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    // The rpc service
    rpcServer.callHandler(ProducerServiceGrpc.getStreamingInputCallMethod(), request -> {
      request.handler(payload -> {
        System.out.println(payload.getPayload().getType().getNumber());
      }).endHandler(v -> {
        System.out.println("Request has ended.");
        request.response().end(Messages.StreamingInputCallResponse.newBuilder().build());
      });
    });

    // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure(cause -> {
        cause.printStackTrace();
      });
  }
}

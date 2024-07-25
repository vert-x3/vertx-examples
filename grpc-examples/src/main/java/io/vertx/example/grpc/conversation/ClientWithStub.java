package io.vertx.example.grpc.conversation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxConversationalServiceGrpcClient;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ClientWithStub extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientWithStub.class.getName()});
  }

  @Override
  public void start() {

    // Create the channel
    GrpcClient client = GrpcClient.client(vertx);

    // Get a stub to use for interacting with the remote service
    VertxConversationalServiceGrpcClient stub = new VertxConversationalServiceGrpcClient(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    // Call the remote service
    stub.fullDuplexCall(writeStream -> {
      // start the conversation
      writeStream.write(Messages.StreamingOutputCallRequest.newBuilder().build());
      vertx.setTimer(500L, t -> {
        writeStream.write(Messages.StreamingOutputCallRequest.newBuilder().build());
      });
    }).onSuccess(resp -> {
      resp.handler(msg -> {
        System.out.println("Client: received response");
      });
    });
  }
}

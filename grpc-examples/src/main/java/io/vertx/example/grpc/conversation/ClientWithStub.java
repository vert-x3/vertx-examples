package io.vertx.example.grpc.conversation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxConversationalServiceGrpc;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.client.GrpcClientChannel;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ClientWithStub extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", ClientWithStub.class.getName());
  }

  @Override
  public void start() {

    // Create the channel
    GrpcClient client = GrpcClient.client(vertx);
    GrpcClientChannel channel = new GrpcClientChannel(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    // Get a stub to use for interacting with the remote service
    VertxConversationalServiceGrpc.ConversationalServiceVertxStub stub = VertxConversationalServiceGrpc.newVertxStub(channel);

    // Call the remote service
    stub.fullDuplexCall(writeStream -> {
      // start the conversation
      writeStream.write(Messages.StreamingOutputCallRequest.newBuilder().build());
      vertx.setTimer(500L, t -> {
        writeStream.write(Messages.StreamingOutputCallRequest.newBuilder().build());
      });
    }).handler(req -> {
      System.out.println("Client: received response");
    });
  }
}

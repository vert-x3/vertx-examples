package io.vertx.example.grpc.producer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.VertxProducerServiceGrpcClient;
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
    VertxProducerServiceGrpcClient stub = new VertxProducerServiceGrpcClient(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    // Call the remote service
    stub.streamingInputCall(writeStream -> {
      for (int i = 0; i < 10; i++) {
        writeStream.write(Messages.StreamingInputCallRequest.newBuilder().build());
      }
      writeStream.end();
    }).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("Server replied OK");
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}

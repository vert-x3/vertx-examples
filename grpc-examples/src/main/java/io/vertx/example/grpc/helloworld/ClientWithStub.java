package io.vertx.example.grpc.helloworld;

import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.examples.helloworld.VertxGreeterGrpcClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ClientWithStub extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", ClientWithStub.class.getName());
  }

  @Override
  public void start() {
    GrpcClient client = GrpcClient.client(vertx);
    VertxGreeterGrpcClient stub = new VertxGreeterGrpcClient(client, SocketAddress.inetSocketAddress(8080, "localhost"));
    HelloRequest request = HelloRequest.newBuilder().setName("Julien").build();
    stub.sayHello(request).onComplete(asyncResponse -> {
      if (asyncResponse.succeeded()) {
        System.out.println("Succeeded " + asyncResponse.result().getMessage());
      } else {
        asyncResponse.cause().printStackTrace();
      }
    });
  }
}

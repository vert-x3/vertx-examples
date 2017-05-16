package io.vertx.example.grpc.ssl;

import io.grpc.ManagedChannel;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.JksOptions;
import io.vertx.example.grpc.util.Runner;
import io.vertx.grpc.VertxChannelBuilder;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {
    ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, "localhost", 50051)
      .usePlaintext(true)
      .useSsl(options -> options.setSsl(true)
        .setUseAlpn(true)
        .setTrustStoreOptions(new JksOptions()
          .setPath("tls/client-truststore.jks")
          .setPassword("wibble")))
      .build();
    GreeterGrpc.GreeterVertxStub stub = GreeterGrpc.newVertxStub(channel);
    HelloRequest request = HelloRequest.newBuilder().setName("Julien").build();
    stub.sayHello(request, asyncResponse -> {
      if (asyncResponse.succeeded()) {
        System.out.println("Succeeded " + asyncResponse.result().getMessage());
      } else {
        asyncResponse.cause().printStackTrace();
      }
    });
  }
}

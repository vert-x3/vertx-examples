package io.vertx.example.grpc.ssl;

import io.grpc.examples.helloworld.GreeterGrpcClient;
import io.grpc.examples.helloworld.HelloRequest;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.ClientSSLOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.launcher.application.VertxApplication;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ClientWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ClientWithStub.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() {

    client = GrpcClient.builder(vertx)
      .with(new ClientSSLOptions().setTrustOptions(new JksOptions()
        .setPath("tls/client-truststore.jks")
        .setPassword("wibble"))
      ).build();

    GreeterGrpcClient stub = GreeterGrpcClient.create(client, SocketAddress.inetSocketAddress(8080, "localhost"));

    HelloRequest request = HelloRequest.newBuilder().setName("Julien").build();
    return stub
      .sayHello(request)
      .onSuccess(res -> System.out.println("Succeeded " + res.getMessage()));
  }
}

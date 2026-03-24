package io.vertx.example.grpc.ssl;

import io.grpc.examples.helloworld.GreeterGrpcClient;
import io.grpc.examples.helloworld.HelloRequest;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.ClientSSLOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.GrpcReadStream;
import io.vertx.launcher.application.VertxApplication;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends VerticleBase {

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

    return client.request(SocketAddress.inetSocketAddress(8080, "localhost"), GreeterGrpcClient.SayHello)
      .compose(request -> {
        request.end(HelloRequest.newBuilder().setName("Julien").build());
        return request.response().compose(GrpcReadStream::last);
      })
      .onSuccess(reply -> System.out.println("Succeeded " +reply.getMessage()));
  }
}

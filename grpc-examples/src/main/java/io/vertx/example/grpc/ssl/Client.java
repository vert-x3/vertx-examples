package io.vertx.example.grpc.ssl;

import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.examples.helloworld.VertxGreeterGrpcClient;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpClientOptions;
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

    HttpClientOptions options = new HttpClientOptions()
      .setSsl(true)
      .setUseAlpn(true)
      .setTrustOptions(new JksOptions()
        .setPath("tls/client-truststore.jks")
        .setPassword("wibble"));

    client = GrpcClient.client(vertx, options);

    return client.request(SocketAddress.inetSocketAddress(8080, "localhost"), VertxGreeterGrpcClient.SayHello)
      .compose(request -> {
        request.end(HelloRequest.newBuilder().setName("Julien").build());
        return request.response().compose(GrpcReadStream::last);
      })
      .onSuccess(reply -> System.out.println("Succeeded " +reply.getMessage()));
  }
}

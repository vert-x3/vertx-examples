package io.vertx.example.grpc.ssl;

import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.GrpcReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", ClientWithStub.class.getName());
  }

  @Override
  public void start() {

    HttpClientOptions options = new HttpClientOptions()
      .setSsl(true)
      .setUseAlpn(true)
      .setTrustStoreOptions(new JksOptions()
        .setPath("tls/client-truststore.jks")
        .setPassword("wibble"));
    GrpcClient client = GrpcClient.client(vertx, options);
    client.request(SocketAddress.inetSocketAddress(8080, "localhost"), GreeterGrpc.getSayHelloMethod())
      .compose(request -> {
        request.end(HelloRequest.newBuilder().setName("Julien").build());
        return request.response().compose(GrpcReadStream::last);
      })
      .onSuccess(reply -> System.out.println("Succeeded " +reply.getMessage()))
      .onFailure(Throwable::printStackTrace);
  }
}

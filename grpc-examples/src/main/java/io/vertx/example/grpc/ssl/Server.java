package io.vertx.example.grpc.ssl;

import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.net.JksOptions;
import io.vertx.example.grpc.util.Runner;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    VertxServer server = VertxServerBuilder.forPort(vertx, 50051)
      .addService(new GreeterGrpc.GreeterVertxImplBase() {
      @Override
      public void sayHello(HelloRequest request, Future<HelloReply> future) {
        System.out.println("Hello " + request.getName());
        future.complete(HelloReply.newBuilder().setMessage(request.getName()).build());
      }
    })
      .useSsl(options -> options
        .setSsl(true)
        .setUseAlpn(true)
        .setKeyStoreOptions(new JksOptions()
          .setPath("tls/server-keystore.jks")
          .setPassword("wibble")))
      .build();
    server.start(ar -> {
      if (ar.succeeded()) {
        System.out.println("gRPC service started");
      } else {
        System.out.println("Could not start server " + ar.cause().getMessage());
      }
    });
  }
}

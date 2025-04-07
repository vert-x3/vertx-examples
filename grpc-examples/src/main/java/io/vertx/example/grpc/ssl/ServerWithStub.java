package io.vertx.example.grpc.ssl;

import io.grpc.examples.helloworld.GreeterGrpcService;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerWithStub.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {
    GreeterGrpcService service = new GreeterGrpcService() {
      @Override
      public Future<HelloReply> sayHello(HelloRequest request) {
        System.out.println("Hello " + request.getName());
        return Future.succeededFuture(HelloReply.newBuilder().setMessage(request.getName()).build());
      }
    };

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    // Bind the service
    rpcServer.addService(service);

    // start the server
    HttpServerOptions options = new HttpServerOptions()
      .setSsl(true)
      .setUseAlpn(true)
      .setKeyCertOptions(new JksOptions()
        .setPath("tls/server-keystore.jks")
        .setPassword("wibble"));
    return vertx
      .createHttpServer(options)
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

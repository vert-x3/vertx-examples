package io.vertx.example.grpc.ssl;

import io.grpc.examples.helloworld.GreeterGrpcService;
import io.grpc.examples.helloworld.HelloReply;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.ServerSSLOptions;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {
    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    // The rpc service
    rpcServer.callHandler(GreeterGrpcService.SayHello, request -> {
      request
        .last()
        .onSuccess(msg -> {
          System.out.println("Hello " + msg.getName());
          request.response().end(HelloReply.newBuilder().setMessage(msg.getName()).build());
        });
    });

    // start the server
    ServerSSLOptions sslOptions = new ServerSSLOptions()
      .setKeyCertOptions(new JksOptions()
      .setPath("tls/server-keystore.jks")
      .setPassword("wibble"));

    return vertx.createHttpServer(sslOptions).requestHandler(rpcServer).listen(8080);
  }
}

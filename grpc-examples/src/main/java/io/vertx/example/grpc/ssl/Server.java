package io.vertx.example.grpc.ssl;

import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.VertxGreeterGrpcServer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public void start() {
    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    // The rpc service
    rpcServer.callHandler(VertxGreeterGrpcServer.SayHello, request -> {
      request
        .last()
        .onSuccess(msg -> {
          System.out.println("Hello " + msg.getName());
          request.response().end(HelloReply.newBuilder().setMessage(msg.getName()).build());
        });
    });

    // start the server
    HttpServerOptions options = new HttpServerOptions()
      .setSsl(true)
      .setUseAlpn(true)
      .setKeyCertOptions(new JksOptions()
        .setPath("tls/server-keystore.jks")
        .setPassword("wibble"));
    vertx.createHttpServer(options).requestHandler(rpcServer).listen(8080)
      .onFailure(cause -> {
        cause.printStackTrace();
      });
  }
}

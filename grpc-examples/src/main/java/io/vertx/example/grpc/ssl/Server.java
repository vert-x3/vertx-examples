package io.vertx.example.grpc.ssl;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.ServerSSLOptions;
import io.vertx.example.grpc.ExampleServiceGrpcService;
import io.vertx.example.grpc.Response;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {
    GrpcServer rpcServer = GrpcServer.server(vertx);

    rpcServer.callHandler(ExampleServiceGrpcService.Unary, request -> {
      request
        .last()
        .onSuccess(msg -> {
          System.out.println("Hello " + msg.getValue());
          request.response().end(Response.newBuilder().setValue(msg.getValue()).build());
        });
    });

    ServerSSLOptions sslOptions = new ServerSSLOptions()
      .setKeyCertOptions(new JksOptions()
      .setPath("tls/server-keystore.jks")
      .setPassword("wibble"));

    return vertx.createHttpServer(sslOptions).requestHandler(rpcServer).listen(8080);
  }
}

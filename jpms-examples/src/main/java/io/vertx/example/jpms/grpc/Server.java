package io.vertx.example.jpms.grpc;

import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.examples.helloworld.VertxGreeterGrpcServer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.grpc.server.GrpcServer;

public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Server())
      .onFailure(Throwable::printStackTrace);
  }

  @Override
  public void start(Promise<Void> startFuture) {
    HttpServer server = vertx.createHttpServer(
      new HttpServerOptions()
        .setUseAlpn(true)
        .setKeyCertOptions(new JksOptions().setPath("server-keystore.jks").setPassword("wibble"))
        .setSsl(false)
    );
    GrpcServer grpcServer = GrpcServer.server(vertx);
    VertxGreeterGrpcServer.GreeterApi api = new VertxGreeterGrpcServer.GreeterApi() {
      @Override
      public Future<HelloReply> sayHello(HelloRequest request) {
        return vertx.timer(200)
                .map(v -> HelloReply
                        .newBuilder()
                        .setMessage("Hello " + request.getName())
                        .build());
      }
    };
    api.bind_sayHello(grpcServer);
    server
      .requestHandler(grpcServer)
      .listen(8080)
      .<Void>mapEmpty()
      .onComplete(startFuture);
  }
}

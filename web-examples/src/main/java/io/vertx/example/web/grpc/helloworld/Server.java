package io.vertx.example.web.grpc.helloworld;

import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
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
    rpcServer.callHandler(GreeterGrpc.getSayHelloMethod(), request -> {
      request
        .last()
        .onSuccess(msg -> {
          request.response().end(HelloReply.newBuilder().setMessage(msg.getName()).build());
      });
    });

    Router router = Router.router(vertx);

    // Route gRPC to the rpc server
    router.route().consumes("application/grpc").handler(rc -> rpcServer.handle(rc.request()));


    // start the server
    vertx.createHttpServer().requestHandler(router).listen(8080)
      .onFailure(cause -> {
        cause.printStackTrace();
      });
  }
}

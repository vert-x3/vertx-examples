package io.vertx.example.grpc.empty;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.example.grpc.EmptyProtos;
import io.vertx.example.grpc.VertxEmptyPingPongServiceGrpcServer;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ServerWithStub extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerWithStub.class.getName()});
  }

  @Override
  public void start() {

    // The rpc service
    VertxEmptyPingPongServiceGrpcServer.EmptyPingPongServiceApi service = new VertxEmptyPingPongServiceGrpcServer.EmptyPingPongServiceApi() {
      @Override
      public Future<EmptyProtos.Empty> emptyCall(EmptyProtos.Empty request) {
        return Future.succeededFuture(EmptyProtos.Empty.newBuilder().build());
      }
    };

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    //


    // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure(cause -> {
        cause.printStackTrace();
      });
  }
}

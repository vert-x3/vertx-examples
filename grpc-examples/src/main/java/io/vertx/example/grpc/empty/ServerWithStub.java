package io.vertx.example.grpc.empty;

import io.grpc.stub.StreamObserver;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.grpc.EmptyPingPongServiceGrpc;
import io.vertx.example.grpc.EmptyProtos;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.grpc.server.GrpcServiceBridge;
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
    EmptyPingPongServiceGrpc.EmptyPingPongServiceImplBase service = new EmptyPingPongServiceGrpc.EmptyPingPongServiceImplBase() {
      @Override
      public void emptyCall(EmptyProtos.Empty request, StreamObserver<EmptyProtos.Empty> responseObserver) {
        responseObserver.onNext(EmptyProtos.Empty.newBuilder().build());
        responseObserver.onCompleted();
      }
    };

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);
    GrpcServiceBridge
      .bridge(service)
      .bind(rpcServer);

    // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure(cause -> {
        cause.printStackTrace();
      });
  }
}

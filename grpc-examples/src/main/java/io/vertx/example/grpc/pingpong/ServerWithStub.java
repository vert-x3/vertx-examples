package io.vertx.example.grpc.pingpong;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.PingPongServiceGrpcService;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class ServerWithStub extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerWithStub.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() {

    // The rpc service
    PingPongServiceGrpcService service = new PingPongServiceGrpcService() {
      @Override
      public Future<Messages.SimpleResponse> unaryCall(Messages.SimpleRequest request) {
        return Future.succeededFuture(Messages.SimpleResponse.newBuilder().setUsername("Paulo").build());
      }
    };

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    // Bind the service
    rpcServer.addService(service);

    // start the server
    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

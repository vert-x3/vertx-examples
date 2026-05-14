package io.vertx.example.grpc.eventbus;

import io.grpc.examples.helloworld.GreeterGrpcService;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.grpc.eventbus.EventBusGrpcServer;
import io.vertx.launcher.application.VertxApplication;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName(), "-cluster"});
  }

  @Override
  public Future<?> start() throws Exception {
    GreeterGrpcService service = new GreeterGrpcService() {
      @Override
      public Future<HelloReply> sayHello(HelloRequest request) {
        System.out.println("Hello " + request.getName());
        return Future.succeededFuture(HelloReply.newBuilder().setMessage(request.getName()).build());
      }
    };

    // Create the server
    EventBusGrpcServer rpcServer = EventBusGrpcServer.create(vertx);

    // Bind the service
    rpcServer.addService(service);

    return super.start();
  }
}

package io.vertx.example.grpc.eventbus;

import io.grpc.examples.helloworld.GreeterGrpcClient;
import io.grpc.examples.helloworld.HelloRequest;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.grpc.eventbus.EventBusGrpcClient;
import io.vertx.launcher.application.VertxApplication;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class UnaryClient extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{UnaryClient.class.getName(), "-cluster"});
  }

  private EventBusGrpcClient client;

  @Override
  public Future<?> start() {
    return EventBusGrpcClient.client(vertx)
      .andThen(ar -> {
        if (ar.succeeded()) {
          client = ar.result();
        }
      }).compose(client -> {
        GreeterGrpcClient stub = GreeterGrpcClient.create(client);
        HelloRequest request = HelloRequest.newBuilder().setName("Julien").build();
        return stub
          .sayHello(request)
          .onSuccess(res -> System.out.println("Succeeded " + res.getMessage()));
      });
  }
}

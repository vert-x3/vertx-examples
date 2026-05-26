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
public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName(), "-cluster"});
  }

  private EventBusGrpcClient client;

  @Override
  public Future<?> start() {
    client = EventBusGrpcClient.client(vertx);
    GreeterGrpcClient stub = GreeterGrpcClient.create(client);
    HelloRequest request = HelloRequest.newBuilder().setName("Julien").build();
    return stub
      .sayHello(request)
      .onSuccess(res -> System.out.println("Succeeded " + res.getMessage()));
  }
}

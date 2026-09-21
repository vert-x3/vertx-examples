package io.vertx.example.grpc.eventbus;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.grpc.eventbus.EventBusGrpcClient;
import io.vertx.launcher.application.VertxApplication;

public class BidiStreamingClient extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{BidiStreamingClient.class.getName(), "-cluster"});
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
        ExampleServiceGrpcClient stub = ExampleServiceGrpcClient.create(client);

        return stub.bidiStreaming((writeStream, err) -> {
          writeStream.write(Request.newBuilder().setValue("ping").build());
          vertx.setTimer(500L, t -> {
            writeStream.write(Request.newBuilder().setValue("ping").build());
          });
        }).onSuccess(resp -> {
          resp.handler(msg -> System.out.println("Client: received response " + msg.getValue()));
        });
      });
  }
}

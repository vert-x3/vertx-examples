package io.vertx.example.grpc.eventbus;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.ConversationalServiceGrpcClient;
import io.vertx.example.grpc.Messages;
import io.vertx.grpc.eventbus.EventBusGrpcClient;
import io.vertx.launcher.application.VertxApplication;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StreamingClient extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{StreamingClient.class.getName(), "-cluster"});
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
        // Get a stub to use for interacting with the remote service
        ConversationalServiceGrpcClient stub = ConversationalServiceGrpcClient.create(client);

        // Call the remote service
        return stub.fullDuplexCall((writeStream, err) -> {
          // start the conversation
          writeStream.write(Messages.StreamingOutputCallRequest.newBuilder().build());
          vertx.setTimer(500L, t -> {
            writeStream.write(Messages.StreamingOutputCallRequest.newBuilder().build());
          });
        }).onSuccess(resp -> {
          resp.handler(msg -> System.out.println("Client: received response"));
        });
      });
  }
}

package io.vertx.example.grpc.eventbus;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.grpc.ConversationalServiceGrpcService;
import io.vertx.example.grpc.Messages;
import io.vertx.grpc.eventbus.EventBusGrpcServer;
import io.vertx.launcher.application.VertxApplication;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class StreamingServer extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{StreamingServer.class.getName(), "-cluster"});
  }

  @Override
  public Future<?> start() throws Exception {

    // The rpc service
    ConversationalServiceGrpcService service = new ConversationalServiceGrpcService() {
      @Override
      protected void fullDuplexCall(ReadStream<Messages.StreamingOutputCallRequest> request, WriteStream<Messages.StreamingOutputCallResponse> response) {
        request
          .handler(req -> {
            System.out.println("Server: received request");
            vertx.setTimer(500L, t -> {
              response.write(Messages.StreamingOutputCallResponse.newBuilder().build());
            });
          });
      }
    };

    // Create the server
    return EventBusGrpcServer
      .server(vertx)
      .onSuccess(rpcServer -> {
        // Bind the service
        rpcServer.addService(service);
    });
  }
}

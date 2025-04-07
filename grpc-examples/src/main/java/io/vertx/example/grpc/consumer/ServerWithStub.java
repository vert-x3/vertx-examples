package io.vertx.example.grpc.consumer;

import com.google.protobuf.ByteString;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.grpc.ConsumerServiceGrpcService;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.Messages.PayloadType;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

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
    ConsumerServiceGrpcService service = new ConsumerServiceGrpcService() {
      @Override
      protected void streamingOutputCall(Messages.StreamingOutputCallRequest request, WriteStream<Messages.StreamingOutputCallResponse> response) {
        final AtomicInteger counter = new AtomicInteger();
        vertx.setPeriodic(1000L, t -> {
          response.write(Messages.StreamingOutputCallResponse.newBuilder().setPayload(
            Messages.Payload.newBuilder()
              .setTypeValue(PayloadType.COMPRESSABLE.getNumber())
              .setBody(ByteString.copyFrom(
                String.valueOf(counter.incrementAndGet()), StandardCharsets.UTF_8))
          ).build());
        });
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

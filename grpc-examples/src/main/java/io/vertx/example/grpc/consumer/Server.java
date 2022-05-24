package io.vertx.example.grpc.consumer;

import com.google.protobuf.ByteString;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.Messages.PayloadType;
import io.vertx.example.grpc.VertxConsumerServiceGrpc;
import io.vertx.example.util.Runner;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.grpc.server.GrpcServiceBridge;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() {

    // The rpc service
    VertxConsumerServiceGrpc.ConsumerServiceVertxImplBase service = new VertxConsumerServiceGrpc.ConsumerServiceVertxImplBase() {
      @Override
      public void streamingOutputCall(Messages.StreamingOutputCallRequest request, WriteStream<Messages.StreamingOutputCallResponse> response) {
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

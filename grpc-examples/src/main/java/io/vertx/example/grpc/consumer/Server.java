package io.vertx.example.grpc.consumer;

import com.google.protobuf.ByteString;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.example.grpc.ConsumerServiceGrpc;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.Messages.PayloadType;
import io.vertx.grpc.server.GrpcServer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() {

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    // The rpc service
    rpcServer.callHandler(ConsumerServiceGrpc.getStreamingOutputCallMethod(), request -> {
      final AtomicInteger counter = new AtomicInteger();
      vertx.setPeriodic(1000L, t -> {
        request.response().write(Messages.StreamingOutputCallResponse.newBuilder().setPayload(
          Messages.Payload.newBuilder()
            .setTypeValue(PayloadType.COMPRESSABLE.getNumber())
            .setBody(ByteString.copyFrom(
              String.valueOf(counter.incrementAndGet()), StandardCharsets.UTF_8))
        ).build());
      });
    });

    // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure(cause -> {
        cause.printStackTrace();
      });
  }
}

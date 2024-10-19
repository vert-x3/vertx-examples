package io.vertx.example.grpc.consumer;

import com.google.protobuf.ByteString;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.Messages.PayloadType;
import io.vertx.example.grpc.VertxConsumerServiceGrpcServer;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public Future<?> start() {

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);

    // The rpc service
    rpcServer.callHandler(VertxConsumerServiceGrpcServer.StreamingOutputCall, request -> {
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
    return vertx
      .createHttpServer()
      .requestHandler(rpcServer)
      .listen(8080);
  }
}

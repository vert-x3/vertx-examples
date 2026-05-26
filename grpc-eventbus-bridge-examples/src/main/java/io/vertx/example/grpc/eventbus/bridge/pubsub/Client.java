package io.vertx.example.grpc.eventbus.bridge.pubsub;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.Json;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.event.v1alpha.EventBusBridgeGrpcClient;
import io.vertx.grpc.event.v1alpha.JsonValueFormat;
import io.vertx.grpc.event.v1alpha.SubscribeOp;
import io.vertx.launcher.application.VertxApplication;

public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public Future<?> start() {
    GrpcClient client = GrpcClient.client(vertx);
    SocketAddress serverAddress = SocketAddress.inetSocketAddress(8080, "localhost");
    EventBusBridgeGrpcClient bridgeClient = EventBusBridgeGrpcClient.create(client, serverAddress);

    // Subscribe to the "news" address
    SubscribeOp request = SubscribeOp.newBuilder()
      .setAddress("news")
      .setMessageBodyFormatValue(JsonValueFormat.text_VALUE)
      .build();

    return bridgeClient.subscribe(request)
      .onSuccess(stream -> {
        stream.handler(message -> {
          Object messageBody = Json.decodeValue(message.getBody().getText());
          System.out.println("Received news: " + messageBody);
        });
        stream.exceptionHandler(err -> {
          System.err.println("Stream error: " + err.getMessage());
        });
      })
      .onFailure(err -> System.err.println("Failed to subscribe: " + err.getMessage()));
  }
}

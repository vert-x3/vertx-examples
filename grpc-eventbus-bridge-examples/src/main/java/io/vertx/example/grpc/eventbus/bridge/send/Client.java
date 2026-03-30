package io.vertx.example.grpc.eventbus.bridge.send;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.event.v1alpha.EventBusBridgeGrpcClient;
import io.vertx.grpc.event.v1alpha.JsonValue;
import io.vertx.grpc.event.v1alpha.SendOp;
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

    // Create a message
    JsonObject message = new JsonObject().put("value", "Hello from gRPC client");
    JsonValue body = JsonValue.newBuilder().setText(message.encode()).build();

    // Send the message to the "hello" address
    SendOp request = SendOp.newBuilder()
      .setAddress("hello")
      .setBody(body)
      .build();

    return bridgeClient.send(request)
      .onSuccess(v -> System.out.println("Message sent successfully"))
      .onFailure(err -> System.err.println("Failed to send message: " + err.getMessage()));
  }
}

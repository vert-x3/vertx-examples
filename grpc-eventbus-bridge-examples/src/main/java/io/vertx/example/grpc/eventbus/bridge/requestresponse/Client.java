package io.vertx.example.grpc.eventbus.bridge.requestresponse;

import com.google.protobuf.Duration;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.event.v1alpha.EventBusBridgeGrpcClient;
import io.vertx.grpc.event.v1alpha.JsonValue;
import io.vertx.grpc.event.v1alpha.JsonValueFormat;
import io.vertx.grpc.event.v1alpha.RequestOp;
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

    // Create the request with timeout
    RequestOp request = RequestOp.newBuilder()
      .setAddress("echo")
      .setBody(body)
      .setReplyBodyFormatValue(JsonValueFormat.text_VALUE)
      .setTimeout(Duration.newBuilder().setSeconds(10).build())
      .build();

    // Send the request and receive the response
    return bridgeClient.request(request)
      .onSuccess(response -> {
        Object responseBody = Json.decodeValue(response.getBody().getText());
        System.out.println("Received response: " + responseBody);
      })
      .onFailure(err -> System.err.println("Request failed: " + err.getMessage()));
  }
}

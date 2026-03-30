package io.vertx.example.grpc.eventbus.bridge.pubsub;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.JsonObject;
import io.vertx.eventbus.bridge.grpc.GrpcBridgeOptions;
import io.vertx.eventbus.bridge.grpc.GrpcEventBusBridge;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.launcher.application.VertxApplication;

public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public Future<?> start() {
    // Configure bridge options with outbound permission
    GrpcBridgeOptions options = new GrpcBridgeOptions()
      .addOutboundPermitted(new PermittedOptions().setAddress("news"));

    // Create the bridge
    GrpcEventBusBridge bridge = GrpcEventBusBridge.create(vertx, options);

    // Create the gRPC server and add the bridge service
    GrpcServer grpcServer = GrpcServer.server(vertx);
    grpcServer.addService(bridge);

    // Periodically publish news on the EventBus
    vertx.setPeriodic(1000, id -> {
      JsonObject news = new JsonObject()
        .put("title", "News at " + System.currentTimeMillis());
      vertx.eventBus().publish("news", news.encode());
    });

    // Start the HTTP server hosting the gRPC server
    return vertx
      .createHttpServer()
      .requestHandler(grpcServer)
      .listen(8080)
      .onSuccess(v -> System.out.println("gRPC EventBus Bridge started on port 8080"));
  }
}

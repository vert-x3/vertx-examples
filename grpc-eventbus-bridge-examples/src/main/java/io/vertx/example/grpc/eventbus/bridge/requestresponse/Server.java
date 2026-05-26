package io.vertx.example.grpc.eventbus.bridge.requestresponse;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
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
    // Register an EventBus consumer that echoes back the message
    vertx.eventBus().consumer("echo", message -> {
      System.out.println("Received request: " + message.body());
      message.reply(message.body());
    });

    // Configure bridge options with inbound permission
    GrpcBridgeOptions options = new GrpcBridgeOptions()
      .addInboundPermitted(new PermittedOptions().setAddress("echo"));

    // Create the bridge
    GrpcEventBusBridge bridge = GrpcEventBusBridge.create(vertx, options);

    // Create the gRPC server and add the bridge service
    GrpcServer grpcServer = GrpcServer.server(vertx);
    grpcServer.addService(bridge);

    // Start the HTTP server hosting the gRPC server
    return vertx
      .createHttpServer()
      .requestHandler(grpcServer)
      .listen(8080)
      .onSuccess(v -> System.out.println("gRPC EventBus Bridge started on port 8080"));
  }
}

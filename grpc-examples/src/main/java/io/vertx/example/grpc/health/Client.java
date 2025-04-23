package io.vertx.example.grpc.health;

import io.grpc.health.v1.HealthGrpcClient;
import io.grpc.health.v1.HealthListRequest;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.GrpcReadStream;
import io.vertx.launcher.application.VertxApplication;

public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() throws Exception {
    client = GrpcClient.client(vertx);
    return client.request(SocketAddress.inetSocketAddress(8080, "localhost"), HealthGrpcClient.List)
      .compose(request -> {
        request.end(HealthListRequest.newBuilder().build());
        return request.response().compose(GrpcReadStream::last).map(response -> {
          response.getStatusesMap().forEach((service, status) -> {
            System.out.println("Service " + service + " is " + status.getStatus().name());
          });
          return null;
        });
      });
  }
}

package io.vertx.example.grpc.jsonformat;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.GrpcReadStream;
import io.vertx.grpc.common.WireFormat;
import io.vertx.launcher.application.VertxApplication;

public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() throws Exception {
    client = GrpcClient.client(vertx);
    return client.request(SocketAddress.inetSocketAddress(8080, "localhost"), ExampleServiceGrpcClient.Unary)
      .compose(request -> {
        request.format(WireFormat.JSON);
        request.end(Request.newBuilder().setValue("Julien").build());
        return request.response().compose(GrpcReadStream::last);
      });
  }
}

package io.vertx.example.grpc.loadbalancing;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.Address;
import io.vertx.core.net.AddressResolver;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.net.endpoint.LoadBalancer;
import io.vertx.example.grpc.ExampleServiceGrpcClient;
import io.vertx.example.grpc.Request;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.GrpcReadStream;
import io.vertx.launcher.application.VertxApplication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Client extends VerticleBase {

  private static final AddressResolver resolver = AddressResolver.mappingResolver(Collections
    .<Address, List<SocketAddress>>singletonMap(SocketAddress.inetSocketAddress(80, "service.com"),
      Arrays.asList(
        SocketAddress.inetSocketAddress(8080, "localhost"),
        SocketAddress.inetSocketAddress(8081, "localhost"),
        SocketAddress.inetSocketAddress(8082, "localhost")))::get);

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private GrpcClient client;

  @Override
  public Future<?> start() throws Exception {

    LoadBalancer loadBalancer = LoadBalancer.RANDOM;

    client = GrpcClient
      .builder(vertx)
      .withAddressResolver(resolver)
      .withLoadBalancer(loadBalancer)
      .build();

    return client.request(SocketAddress.inetSocketAddress(80, "service.com"), ExampleServiceGrpcClient.Unary)
      .compose(request -> {
        System.out.println("Interacting with server " + request.connection().remoteAddress());
        request.end(Request.newBuilder().setValue("Julien").build());
        return request.response().compose(GrpcReadStream::last);
      });
  }
}

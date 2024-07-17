package io.vertx.example.grpc.pingpong;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.SocketAddress;
import io.vertx.example.grpc.Messages;
import io.vertx.example.grpc.PingPongServiceGrpc;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.GrpcReadStream;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public void start() {

    // Create the client
    GrpcClient client = GrpcClient.client(vertx);

    // Call the remote service
    client.request(SocketAddress.inetSocketAddress(8080, "localhost"), PingPongServiceGrpc.getUnaryCallMethod())
      .compose(request -> {
        request.end(Messages.SimpleRequest.newBuilder().setFillUsername(true).build());
        return request.response().compose(GrpcReadStream::last);
      })
      .onSuccess(reply -> System.out.println("My username is: " +reply.getUsername()))
      .onFailure(Throwable::printStackTrace);
  }
}

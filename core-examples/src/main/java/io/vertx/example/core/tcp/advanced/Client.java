package io.vertx.example.core.tcp.advanced;

import io.netty.handler.codec.LengthFieldPrepender;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.internal.net.NetSocketInternal;
import io.vertx.core.net.NetSocket;
import io.vertx.launcher.application.VertxApplication;

public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public Future<?> start() throws Exception {
    vertx
      .createNetClient()
      .connect(1234, "localhost")
      .onComplete(res -> {

        if (res.succeeded()) {
          NetSocket socket = res.result();
          configurePipeline(socket);

          socket.handler(buffer -> {
            System.out.println("Net client receiving: " + buffer.toString("UTF-8"));
          });

          // Now send some data
          for (int i = 0; i < 10; i++) {
            String str = "hello " + i + "\n";
            System.out.println("Net client sending: " + str);
            socket.write(str);
          }
        } else {
          System.out.println("Failed to connect " + res.cause());
        }
      });

    return super.start();
  }

  private void configurePipeline(NetSocket socket) {
    NetSocketInternal ns = (NetSocketInternal) socket;
    LengthFieldPrepender decoder = new LengthFieldPrepender(2);
    ns.channelHandlerContext().pipeline().addBefore("handler", "codec", decoder);
  }
}

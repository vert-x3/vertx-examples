package io.vertx.example.core.net.advanced;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.internal.net.NetSocketInternal;
import io.vertx.core.net.NetSocket;
import io.vertx.launcher.application.VertxApplication;

public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public Future<?> start() throws Exception {

    return vertx
      .createNetServer()
      .connectHandler(sock -> {
        configurePipeline(sock);
        sock.pipeTo(sock);

      }).listen(1234)
      .onSuccess(v -> System.out.println("Echo server is now listening"));
  }

  private void configurePipeline(NetSocket socket) {
    NetSocketInternal ns = (NetSocketInternal) socket;
    LengthFieldBasedFrameDecoder codec = new LengthFieldBasedFrameDecoder(1024, 0, 2);
    ns.channelHandlerContext().pipeline().addBefore("handler", "codec", codec);
  }
}

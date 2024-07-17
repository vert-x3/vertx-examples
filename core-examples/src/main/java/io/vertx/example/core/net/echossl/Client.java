package io.vertx.example.core.net.echossl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public void start() throws Exception {

    NetClientOptions options = new NetClientOptions().setSsl(true).setTrustAll(true);

    vertx
      .createNetClient(options).connect(1234, "localhost")
      .onComplete(res -> {
      if (res.succeeded()) {
        NetSocket sock = res.result();
        sock.handler(buff -> {
          System.out.println("client receiving " + buff.toString("UTF-8"));
        });

        // Now send some data
        for (int i = 0; i < 10; i++) {
          String str = "hello " + i + "\n";
          System.out.println("Net client sending: " + str);
          sock.write(str);
        }
      } else {
        System.out.println("Failed to connect " + res.cause());
      }
    });
  }
}

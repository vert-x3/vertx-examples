package io.vertx.example.core.ssl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.createNetClient(new NetClientOptions().setSsl(true).setTrustAll(true)).connect(1234, "localhost", asyncResult -> {
      if (asyncResult.succeeded()) {
        NetSocket sock = asyncResult.result();
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
        System.out.println("Failed to connect " + asyncResult.cause());
      }
    });
  }
}

package io.vertx.example.core.net.echossl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    NetClientOptions options = new NetClientOptions().setSsl(true).setTrustAll(true)
      .setKeyStoreOptions(new JksOptions().setPath("server-keystore.jks")
        .setPassword("wibble"));

    vertx.createNetClient(options).connect(1234, "localhost", res -> {
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

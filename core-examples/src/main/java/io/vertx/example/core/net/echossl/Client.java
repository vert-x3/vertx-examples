package io.vertx.example.core.net.echossl;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.ClientSSLOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.TcpClientConfig;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public Future<?> start() throws Exception {

    TcpClientConfig config = new TcpClientConfig()
      .setSsl(true);

    ClientSSLOptions sslOptions = new ClientSSLOptions()
      .setTrustAll(true)
      .setHostnameVerificationAlgorithm("");

    vertx
      .createNetClient(config, sslOptions).connect(1234, "localhost")
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

    return super.start();
  }
}

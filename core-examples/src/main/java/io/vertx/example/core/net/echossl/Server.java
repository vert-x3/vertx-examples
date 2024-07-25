package io.vertx.example.core.net.echossl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public void start() throws Exception {

    NetServerOptions options = new NetServerOptions()
      .setSsl(true)
      .setKeyCertOptions(new JksOptions()
        .setPath("io/vertx/example/core/net/echossl/server-keystore.jks")
        .setPassword("wibble"));

    vertx.createNetServer(options).connectHandler(sock -> {

      // Create a pipe
      sock.pipeTo(sock);

    }).listen(1234);

    System.out.println("Echo server is now listening");
  }
}

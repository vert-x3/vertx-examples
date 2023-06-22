package io.vertx.example.core.net.echossl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.streams.Pump;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() throws Exception {

    NetServerOptions options = new NetServerOptions()
      .setSsl(true)
      .setKeyStoreOptions(new JksOptions()
        .setPath("io/vertx/example/core/net/echossl/server-keystore.jks")
        .setPassword("wibble"));

    vertx.createNetServer(options).connectHandler(sock -> {

      // Create a pump
      Pump.pump(sock, sock).start();

    }).listen(1234);

    System.out.println("Echo server is now listening");
  }
}

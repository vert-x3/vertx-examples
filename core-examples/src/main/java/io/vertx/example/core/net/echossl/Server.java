package io.vertx.example.core.net.echossl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.streams.Pump;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    NetServerOptions options = new NetServerOptions()
      .setSsl(true).setKeyStoreOptions(new JksOptions().setPath("server-keystore.jks").setPassword("wibble"));

    vertx.createNetServer(options).connectHandler(sock -> {

      // Create a pump
      Pump.pump(sock, sock).start();

    }).listen(1234);

    System.out.println("Echo server is now listening");
  }
}

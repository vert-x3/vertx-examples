package io.vertx.example.core.ssl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.streams.Pump;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    NetServer server = vertx.createNetServer(new NetServerOptions().setPort(1234).setHost("localhost")
      .setSsl(true).setKeyStoreOptions(new JksOptions().setPath("server-keystore.jks").setPassword("wibble")));
    server.connectHandler(sock -> Pump.pump(sock, sock).start()).listen((it) -> {
      if (it.failed()) {
        System.out.println("Cannot start server " + it.cause());
      } else {
        System.out.println("deployed server");
      }
    });
  }
}

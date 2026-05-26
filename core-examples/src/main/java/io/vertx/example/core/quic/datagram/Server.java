package io.vertx.example.core.quic.datagram;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.QuicDatagramConfig;
import io.vertx.core.net.QuicServerConfig;
import io.vertx.core.net.ServerSSLOptions;
import io.vertx.launcher.application.VertxApplication;

import java.util.List;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public Future<?> start() throws Exception {

    // Enable datagram extension
    QuicServerConfig config = new QuicServerConfig();
    config
      .getTransportConfig()
      .setDatagramConfig(new QuicDatagramConfig().setEnabled(true));

    ServerSSLOptions sslOptions = new ServerSSLOptions()
      .setApplicationLayerProtocols(List.of("my-protocol"))
      .setKeyCertOptions(new JksOptions()
        .setPath("io/vertx/example/core/server.jks")
        .setPassword("wibble"));

    return vertx
      .createQuicServer(config, sslOptions)
      .connectHandler(connection -> {

        // Create a pipe
        connection.datagramHandler(datagram -> {
          connection.writeDatagram(datagram);
        });

      }).listen(1234)
      .onSuccess(addr -> System.out.println("Echo server is now listening at " + addr));
  }
}

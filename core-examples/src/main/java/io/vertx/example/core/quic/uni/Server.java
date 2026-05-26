package io.vertx.example.core.quic.uni;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.QuicStream;
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

    ServerSSLOptions sslOptions = new ServerSSLOptions()
      .setApplicationLayerProtocols(List.of("my-protocol"))
      .setKeyCertOptions(new JksOptions()
        .setPath("io/vertx/example/core/server.jks")
        .setPassword("wibble"));

    return vertx
      .createQuicServer(sslOptions)
      .connectHandler(connection -> {

        connection.openStream(false).onComplete(ar -> {
          if (ar.succeeded()) {
            QuicStream stream = ar.result();
            // Now send some data
            for (int i = 0; i < 10; i++) {
              String str = "hello " + i + "\n";
              System.out.println("Quic server sending: " + str);
              stream.write(str);
            }
          } else {
            System.out.println("could not open stream");
          }
        });

      }).listen(1234)
      .onSuccess(addr -> System.out.println("Echo server is now listening at " + addr));
  }
}

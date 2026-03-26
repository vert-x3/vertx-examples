package io.vertx.example.core.tcp.echossl;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.ServerSSLOptions;
import io.vertx.core.net.TcpServerConfig;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public Future<?> start() throws Exception {

    TcpServerConfig config = new TcpServerConfig()
      .setSsl(true);

    ServerSSLOptions sslOptions = new ServerSSLOptions()
      .setKeyCertOptions(new JksOptions()
        .setPath("io/vertx/example/core/tcp/echossl/server-keystore.jks")
        .setPassword("wibble"));

    return vertx
      .createNetServer(config, sslOptions)
      .connectHandler(sock -> {

        // Create a pipe
        sock.pipeTo(sock);

      }).listen(1234)
      .onSuccess(v -> System.out.println("Echo server is now listening"));
  }
}

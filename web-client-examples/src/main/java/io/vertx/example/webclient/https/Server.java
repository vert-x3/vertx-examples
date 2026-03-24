package io.vertx.example.webclient.https;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpServerConfig;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.ServerSSLOptions;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
    System.out.println("Server started");
  }

  @Override
  public Future<?> start() throws Exception {

    // Start an SSL/TLS http server
    return vertx.createHttpServer(
      new HttpServerConfig().setSsl(true),
      new ServerSSLOptions().setKeyCertOptions(new JksOptions()
        .setPath("io/vertx/example/webclient/https/server-keystore.jks")
        .setPassword("wibble"))
    ).requestHandler(req -> {

      req.response().end();

    }).listen(8443);
  }
}

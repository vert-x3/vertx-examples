package io.vertx.example.core.http.https;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpServer;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.ServerSSLOptions;
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

    HttpServer server =
      vertx.createHttpServer(new ServerSSLOptions().setKeyCertOptions(
        new JksOptions().setPath("io/vertx/example/core/server.jks").setPassword("wibble")
      ));

    return server.requestHandler(req -> {
      req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>");
    }).listen(8443);
  }
}

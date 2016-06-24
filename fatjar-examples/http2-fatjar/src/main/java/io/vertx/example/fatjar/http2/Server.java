package io.vertx.example.fatjar.http2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    HttpServer server = vertx.createHttpServer(new HttpServerOptions()
        .setSsl(true).setUseAlpn(true)
        .setPemKeyCertOptions(new PemKeyCertOptions()
            .setCertPath("server-cert.pem")
            .setKeyPath("server-key.pem")
    ));

    server.requestHandler(req -> {
      req.response()
          .putHeader("content-type", "text/html")
          .end("Hello using HTTP " + req.version());
    });

    server.listen(8443);
  }
}

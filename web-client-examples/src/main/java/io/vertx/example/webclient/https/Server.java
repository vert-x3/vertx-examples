package io.vertx.example.webclient.https;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public void start() throws Exception {

    // Start an SSL/TLS http server
    vertx.createHttpServer(new HttpServerOptions().setKeyCertOptions(new JksOptions()
        .setPath("io/vertx/example/webclient/https/server-keystore.jks")
      .setPassword("wibble"))
      .setSsl(true)
    ).requestHandler(req -> {

      req.response().end();

    }).listen(8443)
      .onComplete(listenResult -> {
      if (listenResult.failed()) {
        System.out.println("Could not start HTTP server");
        listenResult.cause().printStackTrace();
      } else {
        System.out.println("Server started");
      }
    });
  }
}

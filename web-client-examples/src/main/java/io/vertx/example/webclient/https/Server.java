package io.vertx.example.webclient.https;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.example.util.Runner;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    // Start an SSL/TLS http server
    vertx.createHttpServer(new HttpServerOptions().setKeyStoreOptions(new JksOptions()
      .setPath("server-keystore.jks")
      .setPassword("wibble"))
      .setSsl(true)
    ).requestHandler(req -> {

      req.response().end();

    }).listen(8443, listenResult -> {
      if (listenResult.failed()) {
        System.out.println("Could not start HTTP server");
        listenResult.cause().printStackTrace();
      } else {
        System.out.println("Server started");
      }
    });
  }
}

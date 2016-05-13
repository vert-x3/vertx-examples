package io.vertx.example.web.http2;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.SSLEngine;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  final Image image = new Image("coin.png");

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    router.get("/").handler(ctx -> {
      ctx.response()
              .putHeader("Content-Type", "text/html")
              .end(image.generateHTML(16));
    });

    router.get("/img/:x/:y").handler(ctx -> {
      ctx.response()
              .putHeader("Content-Type", "image/png")
              .end(image.getPixel(Integer.parseInt(ctx.pathParam("x")), Integer.parseInt(ctx.pathParam("y"))));
    });

    vertx.createHttpServer(
            new HttpServerOptions()
                    .setSsl(true)
                    .setUseAlpn(true)
                    .setSslEngine(SSLEngine.JDK)
                    .setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("tls/server-key.pem").setCertPath("tls/server-cert.pem"))).requestHandler(router::accept)
            .listen(8443);
  }
}

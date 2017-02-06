package io.vertx.example.webclient.https;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
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

        // Create the web client and enable SSL/TLS with a trust store
        WebClient client = WebClient.create(vertx,
          new WebClientOptions()
            .setSsl(true)
            .setTrustStoreOptions(new JksOptions()
              .setPath("client-truststore.jks")
              .setPassword("wibble")
            )
        );

        client.get(8443, "localhost", "/")
          .send(ar -> {
          if (ar.succeeded()) {
            HttpResponse<Buffer> response = ar.result();
            System.out.println("Got HTTP response with status " + response.statusCode());
          } else {
            ar.cause().printStackTrace();
          }
        });
      }
    });
  }
}

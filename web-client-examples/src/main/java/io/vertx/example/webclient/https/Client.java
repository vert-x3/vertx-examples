package io.vertx.example.webclient.https;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Client.class.getName());
  }

  @Override
  public void start() throws Exception {

    // Create the web client and enable SSL/TLS with a trust store
    WebClient client = WebClient.create(vertx,
      new WebClientOptions()
        .setSsl(true)
        .setTrustStoreOptions(new JksOptions()
          .setPath("io/vertx/example/webclient/https/client-truststore.jks")
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
}

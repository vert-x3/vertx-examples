package io.vertx.example.webclient.https;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private WebClient client;

  @Override
  public Future<?> start() throws Exception {

    // Create the web client and enable SSL/TLS with a trust store
    client = WebClient.create(vertx,
      new WebClientOptions()
        .setSsl(true)
        .setTrustOptions(new JksOptions()
          .setPath("io/vertx/example/webclient/https/client-truststore.jks")
          .setPassword("wibble")
        )
    );

    return client.get(8443, "localhost", "/")
      .send()
      .onSuccess(response -> System.out.println("Got HTTP response with status " + response.statusCode()));
  }
}

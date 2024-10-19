package io.vertx.example.webclient.queryparams;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.web.client.WebClient;
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

    client = WebClient.create(vertx);

    return client.get(8080, "localhost", "/")
      .addQueryParam("firstName", "Dale")
      .addQueryParam("lastName", "Cooper")
      .addQueryParam("male", "true")
      .send()
      .onSuccess(response -> System.out.println("Got HTTP response with status " + response.statusCode()));
  }
}

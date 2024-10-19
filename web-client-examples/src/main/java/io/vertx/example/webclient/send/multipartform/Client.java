package io.vertx.example.webclient.send.multipartform;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
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

    MultiMap form = MultiMap.caseInsensitiveMultiMap();
    form.add("firstName", "Dale");
    form.add("lastName", "Cooper");
    form.add("male", "true");

    return client
      .post(8080, "localhost", "/")
      .putHeader("content-type", "multipart/form-data")
      .sendForm(form)
      .onSuccess(response -> System.out.println("Got HTTP response with status " + response.statusCode()));
  }
}

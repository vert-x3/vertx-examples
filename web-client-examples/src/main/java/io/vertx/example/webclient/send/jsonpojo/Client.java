package io.vertx.example.webclient.send.jsonpojo;

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

  public static class User {

    public String firstName;
    public String lastName;
    public boolean male;

  }

  private WebClient client;

  @Override
  public Future<?> start() throws Exception {

    client = WebClient.create(vertx);

    User user = new User();
    user.firstName = "Dale";
    user.lastName = "Cooper";
    user.male = true;

    return client
      .put(8080, "localhost", "/")
      .sendJson(user)
      .onSuccess(response -> System.out.println("Got HTTP response with status " + response.statusCode()));
  }
}

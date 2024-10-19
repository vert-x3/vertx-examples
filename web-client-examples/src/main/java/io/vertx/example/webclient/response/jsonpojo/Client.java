package io.vertx.example.webclient.response.jsonpojo;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
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

    WebClient client = WebClient.create(vertx);

    return client.get(8080, "localhost", "/")
      .as(BodyCodec.json(User.class))
      .send()
      .onSuccess(response -> {
        System.out.println("Got HTTP response body");
        User user = response.body();
        System.out.println("FirstName " + user.firstName);
        System.out.println("LastName " + user.lastName);
        System.out.println("Male " + user.male);
      });
  }
}

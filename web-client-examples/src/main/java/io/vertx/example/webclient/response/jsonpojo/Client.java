package io.vertx.example.webclient.response.jsonpojo;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  public static class User {
    public String firstName;
    public String lastName;
    public boolean male;
  }

  @Override
  public void start() throws Exception {

    WebClient client = WebClient.create(vertx);

    client.get(8080, "localhost", "/")
      .as(BodyCodec.json(User.class))
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<User> response = ar.result();
          System.out.println("Got HTTP response body");
          User user = response.body();
          System.out.println("FirstName " + user.firstName);
          System.out.println("LastName " + user.lastName);
          System.out.println("Male " + user.male);
        } else {
          ar.cause().printStackTrace();
        }
      });
  }
}

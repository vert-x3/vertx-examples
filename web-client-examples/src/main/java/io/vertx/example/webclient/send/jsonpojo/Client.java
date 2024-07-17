package io.vertx.example.webclient.send.jsonpojo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  public static class User {

    public String firstName;
    public String lastName;
    public boolean male;

  }

  @Override
  public void start() throws Exception {

    WebClient client = WebClient.create(vertx);

    User user = new User();
    user.firstName = "Dale";
    user.lastName = "Cooper";
    user.male = true;

    client
      .put(8080, "localhost", "/")
      .sendJson(user)
      .onComplete(ar -> {
      if (ar.succeeded()) {
        HttpResponse<Buffer> response = ar.result();
        System.out.println("Got HTTP response with status " + response.statusCode());
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}

package io.vertx.example.webclient.send.jsonpojo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

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

    User user = new User();
    user.firstName = "Dale";
    user.lastName = "Cooper";
    user.male = true;

    client.put(8080, "localhost", "/").sendJson(user, ar -> {
      if (ar.succeeded()) {
        HttpResponse<Buffer> response = ar.result();
        System.out.println("Got HTTP response with status " + response.statusCode());
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}

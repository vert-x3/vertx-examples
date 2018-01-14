package io.vertx.example.webclient.response.clienterrors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  public static class User {
    public String login;
    public Long id;
  }

  @Override
  public void start() throws Exception {

    WebClient client = WebClient.create(vertx);

    client.get(8080, "localhost", "/")
      .as(BodyCodec.json(User.class))
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<User> response = ar.result();
          User user = response.body();
          System.out.println("Got HTTP response body");
          System.out.println(user.toString());
        } else {
          ar.cause().printStackTrace();
        }
      });
  }
}

package io.vertx.example.webclient.send.jsonobject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Client.class.getName());
  }

  @Override
  public void start() throws Exception {

    WebClient client = WebClient.create(vertx);

    JsonObject user = new JsonObject()
      .put("firstName", "Dale")
      .put("lastName", "Cooper")
      .put("male", true);

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

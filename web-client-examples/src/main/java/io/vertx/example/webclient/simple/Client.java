package io.vertx.example.webclient.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
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

    client.get(8080, "localhost", "/").send(ar -> {
      if (ar.succeeded()) {
        HttpResponse<Buffer> response = ar.result();
        System.out.println("Got HTTP response with status " + response.statusCode() + " with data " +
          response.body().toString("ISO-8859-1"));
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}

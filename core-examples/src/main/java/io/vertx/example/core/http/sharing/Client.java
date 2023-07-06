package io.vertx.example.core.http.sharing;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;

/**
 * A client illustrating the round robin made by vert.x. The client send a request to the server periodically and
 * print the received messages.
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Client.class.getName());
  }

  @Override
  public void start() throws Exception {
    vertx.setPeriodic(1000, l -> {
      HttpClient client = vertx.createHttpClient();
      client.request(HttpMethod.GET, 8080, "localhost", "/")
        .compose(req -> req.send()
          .compose(HttpClientResponse::body))
        .onSuccess(body -> System.out.println(body.toString("ISO-8859-1")))
        .onFailure(Throwable::printStackTrace);
    });
  }
}

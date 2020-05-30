package io.vertx.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setSsl(true).setTrustAll(true));

    client
      .get(443, "icanhazdadjoke.com", "/")
      .putHeader("Accept", "text/plain")
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          System.out.println("Got HTTP response with status " + response.statusCode() + " with data "
            + response.body());
        } else {
          ar.cause().printStackTrace();
        }
      });
  }
}

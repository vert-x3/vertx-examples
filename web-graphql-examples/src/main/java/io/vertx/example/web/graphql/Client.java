package io.vertx.example.web.graphql;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.launcher.application.VertxApplication;

import static io.vertx.core.http.HttpResponseExpectation.JSON;
import static io.vertx.core.http.HttpResponseExpectation.SC_OK;

public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public void start() {
    WebClient webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8080));

    JsonObject request = new JsonObject()
      .put("query", "query($secure: Boolean) { allLinks(secureOnly: $secure) { url, postedBy { name } } }")
      .put("variables", new JsonObject().put("secure", true));

    webClient.post("/graphql")
      .as(BodyCodec.jsonObject())
      .sendJsonObject(request)
      .expecting(SC_OK.and(JSON))
      .onComplete(ar -> {

        if (ar.succeeded()) {
          JsonObject response = ar.result().body();
          System.out.println("response = " + response.encodePrettily());
        } else {
          ar.cause().printStackTrace();
        }

      });
  }
}

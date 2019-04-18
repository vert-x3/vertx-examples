package io.vertx.example.web.graphql;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;

public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Client.class.getName());
  }

  @Override
  public void start() {
    WebClient webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8080));

    JsonObject request = new JsonObject()
      .put("query", "query($secure: Boolean) { allLinks(secureOnly: $secure) { url, postedBy { name } } }")
      .put("variables", new JsonObject().put("secure", true));

    webClient.post("/graphql")
      .expect(ResponsePredicate.SC_OK)
      .expect(ResponsePredicate.JSON)
      .as(BodyCodec.jsonObject())
      .sendJsonObject(request, ar -> {

        if (ar.succeeded()) {
          JsonObject response = ar.result().body();
          System.out.println("response = " + response.encodePrettily());
        } else {
          ar.cause().printStackTrace();
        }

      });
  }
}

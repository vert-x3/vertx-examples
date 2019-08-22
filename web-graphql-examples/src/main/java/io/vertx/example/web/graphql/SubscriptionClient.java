package io.vertx.example.web.graphql;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.graphql.ApolloWSMessageType;

public class SubscriptionClient extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", SubscriptionClient.class.getName());
  }

  @Override
  public void start() {
    HttpClient httpClient = vertx.createHttpClient(new HttpClientOptions().setDefaultPort(8080));
    httpClient.webSocket("/graphql").setHandler(websocketRes -> {
      if (websocketRes.succeeded()) {
        WebSocket webSocket = websocketRes.result();

        webSocket.handler(message -> {
          System.out.println(message.toJsonObject().encodePrettily());
        });

        JsonObject request = new JsonObject()
          .put("id", "1")
          .put("type", ApolloWSMessageType.START.getText())
          .put("payload", new JsonObject()
            .put("query", "subscription { links { url, postedBy { name } } }"));
        webSocket.write(request.toBuffer());
      } else {
        websocketRes.cause().printStackTrace();
      }
    });
  }

}

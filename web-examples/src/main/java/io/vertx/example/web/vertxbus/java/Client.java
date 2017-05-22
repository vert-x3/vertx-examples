package io.vertx.example.web.vertxbus.java;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonObject;

public class Client {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    HttpClient client = vertx.createHttpClient();

    client.websocket(8080, "localhost", "/eventbus/websocket", ws -> {
      System.out.println("Connected");
      sendPing(ws);

      vertx.setPeriodic(5000, id -> sendPing(ws));

      // Register
      JsonObject msg = new JsonObject().put("type", "register").put("address", "feed");
      ws.writeFrame(io.vertx.core.http.WebSocketFrame.textFrame(msg.encode(), true));

      ws.handler(System.out::println);
    }, fail -> System.out.println("Failed: " + fail));
  }

  private static void sendPing(WebSocket ws) {
    JsonObject msg = new JsonObject().put("type", "ping");
    ws.writeFrame(io.vertx.core.http.WebSocketFrame.textFrame(msg.encode(), true));
  }

}

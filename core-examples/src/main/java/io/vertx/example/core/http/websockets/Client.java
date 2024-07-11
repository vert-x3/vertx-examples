package io.vertx.example.core.http.websockets;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocketClient;
import io.vertx.core.http.WebSocketConnectOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Client.class.getName());
  }

  @Override
  public void start() throws Exception {
    WebSocketClient client = vertx.createWebSocketClient();

    client
      .webSocket()
      .handler(data -> {
      System.out.println("Received data " + data.toString("ISO-8859-1"));
      client.close();
    }).connect(8080, "localhost", "/some-uri")
      .onSuccess(webSocket -> {
        webSocket.writeBinaryMessage(Buffer.buffer("Hello world"));
      })
      .onFailure(Throwable::printStackTrace);
  }
}

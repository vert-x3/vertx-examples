package io.vertx.example.core.websockets;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class WebSocketsClient extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    HttpClient client = vertx.createHttpClient();

    client.websocket(8080, "localhost", "/some-uri", websocket -> {
      websocket.handler(data -> {
        System.out.println("Received data " + data.toString("ISO-8859-1"));
        client.close();
      });
      websocket.writeMessage(Buffer.buffer("Hello world"));
    });
  }
}

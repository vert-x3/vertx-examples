package io.vertx.example.core.http.websockets;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class WebSocketsClient extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(WebSocketsClient.class);
  }

  @CodeTranslate
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

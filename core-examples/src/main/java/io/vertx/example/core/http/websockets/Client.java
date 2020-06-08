package io.vertx.example.core.http.websockets;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {
    HttpClient client = vertx.createHttpClient();

    client.webSocket(8080, "localhost", "/some-uri").onSuccess(webSocket -> {
      webSocket.handler(data -> {
        System.out.println("Received data " + data.toString("ISO-8859-1"));
        client.close();
      });
      webSocket.writeBinaryMessage(Buffer.buffer("Hello world"));
    }).onFailure(err -> {
      err.printStackTrace();
    });
  }
}

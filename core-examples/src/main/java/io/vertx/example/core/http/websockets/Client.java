package io.vertx.example.core.http.websockets;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Client.class.getName());
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
    }).onFailure(Throwable::printStackTrace);
  }
}

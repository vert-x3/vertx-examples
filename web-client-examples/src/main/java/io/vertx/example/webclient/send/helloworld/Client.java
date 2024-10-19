package io.vertx.example.webclient.send.helloworld;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private WebClient client;

  private static void handle(HttpResponse<Buffer> response) {
    System.out.println("Got HTTP response with status " + response.statusCode());
  }

  @Override
  public Future<?> start() throws Exception {

    client = WebClient.create(vertx);

    Buffer body = Buffer.buffer("Hello World");

    return client
      .put(8080, "localhost", "/")
      .sendBuffer(body)
      .onSuccess(Client::handle);
  }
}

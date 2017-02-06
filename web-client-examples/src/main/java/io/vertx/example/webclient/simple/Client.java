package io.vertx.example.webclient.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

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

    vertx.createHttpServer().requestHandler(req -> {

      req.response().end("Hello World");

    }).listen(8080, listenResult -> {
      if (listenResult.failed()) {
        System.out.println("Could not start HTTP server");
        listenResult.cause().printStackTrace();
      } else {

        WebClient client = WebClient.create(vertx);

        client.get(8080, "localhost", "/").send(ar -> {
          if (ar.succeeded()) {
            HttpResponse<Buffer> response = ar.result();
            System.out.println("Got HTTP response with status " + response.statusCode() + " with data " +
              response.body().toString("ISO-8859-1"));
          } else {
            ar.cause().printStackTrace();
          }
        });
      }
    });
  }
}

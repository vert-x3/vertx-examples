package io.vertx.example.core.http.https;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClientOptions;
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

    // Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

    vertx.createHttpClient(new HttpClientOptions().setSsl(true).setTrustAll(true)).getNow(4443, "localhost", "/", resp -> {
      System.out.println("Got response " + resp.statusCode());
      resp.bodyHandler(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
    });
  }
}

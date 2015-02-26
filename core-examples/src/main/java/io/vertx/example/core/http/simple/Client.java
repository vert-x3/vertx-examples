package io.vertx.example.core.http.simple;

import io.vertx.codetrans.annotations.CodeTranslate;
import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @CodeTranslate
  @Override
  public void start() throws Exception {
    vertx.createHttpClient().getNow(8080, "localhost", "/", resp -> {
      System.out.println("Got response " + resp.statusCode());
      resp.bodyHandler(body -> {
        System.out.println("Got data " + body.toString("ISO-8859-1"));
      });
    });
  }
}

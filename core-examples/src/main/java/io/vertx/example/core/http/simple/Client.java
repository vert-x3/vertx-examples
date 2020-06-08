package io.vertx.example.core.http.simple;

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

  @Override
  public void start() throws Exception {
    vertx.createHttpClient().get(8080, "localhost", "/").compose(resp -> {
      System.out.println("Got response " + resp.statusCode());
      return resp.body();
    }).onSuccess(body -> {
      System.out.println("Got data " + body.toString("ISO-8859-1"));
    }).onFailure(err -> {
      err.printStackTrace();
    });
  }
}

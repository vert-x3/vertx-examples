package io.vertx.example.core.http.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
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
    HttpClientRequest request = vertx.createHttpClient().request(HttpMethod.PUT, 8080, "localhost", "/");
    request.compose(resp -> {
      System.out.println("Got response " + resp.statusCode());
      return resp.body();
    }).onSuccess(body -> {
      System.out.println("Got data " + body.toString("ISO-8859-1"));
    }).onFailure(err -> {
      err.printStackTrace();
    });

    request.setChunked(true);

    for (int i = 0; i < 10; i++) {
      request.write("client-chunk-" + i);
    }

    request.end();
  }
}

package io.vertx.example.core.http2.h2c;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpVersion;
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

    HttpClientOptions options = new HttpClientOptions().setProtocolVersion(HttpVersion.HTTP_2);

    vertx.createHttpClient(options
    ).getNow(8080, "localhost", "/", resp -> {
      System.out.println("Got response " + resp.statusCode() + " with protocol " + resp.version());
      resp.bodyHandler(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
    });
  }
}

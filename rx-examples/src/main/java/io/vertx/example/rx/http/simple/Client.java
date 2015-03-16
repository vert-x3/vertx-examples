package io.vertx.example.rx.http.simple;

import io.vertx.core.http.HttpMethod;
import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;

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
    HttpClientRequest req = client.request(HttpMethod.GET, 8080, "localhost", "/");
    req.toObservable().subscribe(resp -> {
      System.out.println("Got response " + resp.statusCode());
      resp.bodyHandler(body -> {
        System.out.println("Got data " + body.toString("ISO-8859-1"));
      });
    });
    req.end();
  }
}

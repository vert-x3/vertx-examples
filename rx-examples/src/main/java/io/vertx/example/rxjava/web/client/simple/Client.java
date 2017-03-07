package io.vertx.example.rxjava.web.client.simple;

import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import rx.Single;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {
    WebClient client = WebClient.create(vertx);
    Single<HttpResponse<String>> request = client.get(8080, "localhost", "/")
      .as(BodyCodec.string())
      .rxSend();

    // Fire the request
    request.subscribe(resp -> System.out.println("Server content " + resp.body()));

    // Again
    request.subscribe(resp -> System.out.println("Server content " + resp.body()));

    // And again
    request.subscribe(resp -> System.out.println("Server content " + resp.body()));
  }
}

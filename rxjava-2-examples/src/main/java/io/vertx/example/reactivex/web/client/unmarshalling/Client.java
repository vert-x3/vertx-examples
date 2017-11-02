package io.vertx.example.reactivex.web.client.unmarshalling;

import io.vertx.example.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;
import io.reactivex.Single;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  // Unmarshalled response from server
  static class Data {

    public String message;

  }

  @Override
  public void start() throws Exception {

    WebClient client = WebClient.create(vertx);
    Single<HttpResponse<Data>> request = client.get(8080, "localhost", "/")
      .as(BodyCodec.json(Data.class))
      .rxSend();

    // Fire the request
    request.subscribe(resp -> System.out.println("Server content " + resp.body().message));

    // Again
    request.subscribe(resp -> System.out.println("Server content " + resp.body().message));

    // And again
    request.subscribe(resp -> System.out.println("Server content " + resp.body().message));
  }
}

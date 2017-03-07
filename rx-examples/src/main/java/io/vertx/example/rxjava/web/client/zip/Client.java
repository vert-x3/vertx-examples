package io.vertx.example.rxjava.web.client.zip;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;
import io.vertx.rxjava.core.http.HttpClientResponse;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import rx.Observable;
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

    // Create two requests
    WebClient client = WebClient.create(vertx);
    Single<JsonObject> request = client.get(8080, "localhost", "/")
      .as(BodyCodec.jsonObject())
      .rxSend()
      .map(resp -> resp.body());

    // Combine the responses with the zip into a single response
    request
      .zipWith(request, (b1, b2) -> new JsonObject().put("req1", b1).put("req2", b2))
      .subscribe(json -> {
        System.out.println("Got combined result " + json);
      }, err -> {
        err.printStackTrace();
      });
  }
}

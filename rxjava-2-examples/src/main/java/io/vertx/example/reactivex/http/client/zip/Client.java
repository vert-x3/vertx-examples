package io.vertx.example.reactivex.http.client.zip;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpClientResponse;

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
    HttpClient client = vertx.createHttpClient();

    // Send two requests
    Single<HttpClientResponse> req1 = client.rxGet(8080, "localhost", "/");
    Single<HttpClientResponse> req2 = client.rxGet(8080, "localhost", "/");

    // Turn the responses into Single<JsonObject>
    Single<JsonObject> s1 = req1.flatMap(HttpClientResponse::rxBody).map(Buffer::toJsonObject);
    Single<JsonObject> s2 = req1.flatMap(HttpClientResponse::rxBody).map(Buffer::toJsonObject);

    // Combine the responses with the zip into a single response
    s1.zipWith(s2, (b1, b2) -> new JsonObject().put("req1", b1).put("req2", b2)).subscribe(json -> {
      System.out.println("Got combined result " + json);
    }, Throwable::printStackTrace);
  }
}

package io.vertx.example.reactivex.http.client.zip;

import io.reactivex.Flowable;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpClientRequest;
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

    // Create two requests
    HttpClientRequest req1 = client.request(HttpMethod.GET, 8080, "localhost", "/");
    HttpClientRequest req2 = client.request(HttpMethod.GET, 8080, "localhost", "/");

    // Turn the requests responses into Flowable<JsonObject>
    Flowable<JsonObject> obs1 = req1.toFlowable().flatMap(HttpClientResponse::toFlowable).
      map(buf -> new JsonObject(buf.toString("UTF-8")));
    Flowable<JsonObject> obs2 = req2.toFlowable().flatMap(HttpClientResponse::toFlowable).
      map(buf -> new JsonObject(buf.toString("UTF-8")));

    // Combine the responses with the zip into a single response
    obs1.zipWith(obs2, (b1, b2) -> new JsonObject().put("req1", b1).put("req2", b2)).
      subscribe(json -> {
          System.out.println("Got combined result " + json);
        },
        Throwable::printStackTrace);

    req1.end();
    req2.end();
  }
}

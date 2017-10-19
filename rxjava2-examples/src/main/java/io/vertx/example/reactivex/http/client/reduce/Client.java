package io.vertx.example.reactivex.http.client.reduce;

import io.vertx.core.http.HttpMethod;
import io.vertx.example.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpClientRequest;
import io.reactivex.Observable;

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
    HttpClientRequest req = client.request(HttpMethod.GET, 8080, "localhost", "/");
    req.toObservable().

        // Status code check and -> Observable<Buffer>
        flatMap(resp -> {
          if (resp.statusCode() != 200) {
            throw new RuntimeException("Wrong status code " + resp.statusCode());
          }
          return Observable.just(Buffer.buffer()).mergeWith(resp.toObservable());
        }).

        // Reduce all buffers in a single buffer
        reduce(Buffer::appendBuffer).

        // Turn in to a string
        map(buffer -> buffer.toString("UTF-8")).

        // Get a single buffer
        subscribe(data -> System.out.println("Server content " + data));

    // End request
    req.end();
  }
}

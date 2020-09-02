package io.vertx.example.rxjava.http.client.reduce;

import io.vertx.core.http.HttpMethod;
import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.http.HttpClient;
import rx.Observable;

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

    Observable<String> observable = client.rxRequest(HttpMethod.GET, 8080, "localhost", "/")

      // Connect
      .flatMapObservable(req -> req

        // Send request
        .rxSend()

        // Status code check and -> Observable<Buffer>
        .flatMapObservable(resp -> {
          if (resp.statusCode() != 200) {
            return Observable.error(new RuntimeException("Wrong status code " + resp.statusCode()));
          }
          return Observable.just(Buffer.buffer()).mergeWith(resp.toObservable());
        })

        // Reduce all buffers in a single buffer
        .reduce(Buffer::appendBuffer)

        // Turn in to a string
        .map(buffer -> buffer.toString("UTF-8"))
      );

    observable.subscribe(data -> System.out.println("Server content " + data), Throwable::printStackTrace);
  }
}

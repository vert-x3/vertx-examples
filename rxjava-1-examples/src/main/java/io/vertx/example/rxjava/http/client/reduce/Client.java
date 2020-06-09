package io.vertx.example.rxjava.http.client.reduce;

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

    client.rxGet(8080, "localhost", "/")

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

      // Get a single buffer
      .subscribe(data -> System.out.println("Server content " + data), Throwable::printStackTrace);
  }
}

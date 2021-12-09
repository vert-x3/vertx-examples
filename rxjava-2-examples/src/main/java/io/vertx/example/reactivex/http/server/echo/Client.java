package io.vertx.example.reactivex.http.server.echo;

import io.reactivex.Flowable;
import io.vertx.core.http.HttpMethod;
import io.vertx.example.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.MultiMap;
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

    Flowable<Buffer> payload = Flowable.just(Buffer.buffer("hello", "UTF-8"));

    MultiMap headers = MultiMap.caseInsensitiveMultiMap().add("Content-Type", "text/plain");

    client.rxRequest(HttpMethod.PUT, 8080, "localhost", "/")
      .flatMap(req -> {
        req.headers().addAll(headers);
        return req
          .rxSend(payload)
          .flatMap(HttpClientResponse::rxBody);
      })
      .subscribe(buf -> System.out.println(buf.toString("UTF-8")), Throwable::printStackTrace);
  }
}

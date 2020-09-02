package io.vertx.example.rxjava.http.client.unmarshalling;

import io.vertx.core.http.HttpMethod;
import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientResponse;
import rx.Observable;

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
    HttpClient client = vertx.createHttpClient();

    Observable<Data> observable = client
      .rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMapObservable(req -> req
        .rxSend()
        .flatMapObservable(HttpClientResponse::toObservable)
        .lift(RxHelper.unmarshaller(Data.class))
      );

    observable.subscribe(data -> System.out.println("Got response " + data.message), Throwable::printStackTrace);
  }
}

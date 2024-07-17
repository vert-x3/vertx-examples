package io.vertx.example.rxjava3.http.client.unmarshalling;

import io.reactivex.rxjava3.core.Flowable;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.http.HttpClient;
import io.vertx.rxjava3.core.http.HttpClientResponse;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  // Unmarshalled response from server
  static class Data {

    public String message;

  }

  @Override
  public void start() throws Exception {
    HttpClient client = vertx.createHttpClient();

    Flowable<Data> flowable = client
      .rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMapPublisher(req -> req
        .rxSend()
        .flatMapPublisher(HttpClientResponse::toFlowable)
        // Unmarshall the response to the Data object via Jackon
        .map(buffer -> Json.decodeValue(buffer, Data.class))
      );

    flowable.subscribe(data -> System.out.println("Got response " + data.message), Throwable::printStackTrace);
  }
}

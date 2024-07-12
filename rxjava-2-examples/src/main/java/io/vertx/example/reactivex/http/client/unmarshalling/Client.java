package io.vertx.example.reactivex.http.client.unmarshalling;

import io.reactivex.Flowable;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpClientResponse;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Client.class.getName());
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

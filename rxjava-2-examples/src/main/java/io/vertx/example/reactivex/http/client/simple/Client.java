package io.vertx.example.reactivex.http.client.simple;

import io.reactivex.Single;
import io.vertx.core.http.HttpMethod;
import io.vertx.example.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpClient;

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
    Single<Buffer> single = client
      .rxRequest(HttpMethod.GET, 8080, "localhost", "/")
      .flatMap(req -> req
        .rxSend()
        .flatMap(resp -> {
          // Status code check and -> Single<Buffer>
          if (resp.statusCode() != 200) {
            return Single.error(new RuntimeException("Wrong status code " + resp.statusCode()));
          }
          return resp.rxBody();
        }));
    single.subscribe(data -> System.out.println("Server content " + data.toString("UTF-8")), Throwable::printStackTrace);
  }
}

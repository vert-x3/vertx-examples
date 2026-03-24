package io.vertx.example.core.http2.h2c;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.*;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private HttpClient client;

  @Override
  public Future<?> start() throws Exception {

    HttpClientConfig options = new HttpClientConfig().setVersions(HttpVersion.HTTP_2, HttpVersion.HTTP_1_1);

    client = vertx.createHttpClient(options);
    return client
      .request(HttpMethod.GET, 8080, "localhost", "/")
      .compose(req -> req.send()
        .compose(resp -> {
          System.out.println("Got response " + resp.statusCode());
          return resp.body();
        }))
      .onSuccess(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
  }
}

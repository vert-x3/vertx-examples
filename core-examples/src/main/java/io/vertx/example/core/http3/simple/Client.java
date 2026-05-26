package io.vertx.example.core.http3.simple;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientConfig;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.ClientSSLOptions;
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

    // Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

    HttpClientConfig config = new HttpClientConfig().
      setSsl(true).
      setVersions(HttpVersion.HTTP_3, HttpVersion.HTTP_1_1);

    client = vertx.createHttpClient(config, new ClientSSLOptions().setTrustAll(true));

    return client.
      request(HttpMethod.GET, 8443, "localhost", "/")
      .compose(req -> req.send()
        .compose(resp -> {
          System.out.println("Got response " + resp.statusCode());
          return resp.body();
        }))
      .onSuccess(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
  }
}

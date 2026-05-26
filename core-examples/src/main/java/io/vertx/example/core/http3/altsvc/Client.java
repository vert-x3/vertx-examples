package io.vertx.example.core.http3.altsvc;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.dns.AddressResolverOptions;
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
    Vertx vertx = Vertx.vertx(new VertxOptions()
      .setAddressResolverOptions(new AddressResolverOptions()
        // This should not be necessary
        .setHostsValue(Buffer.buffer("127.0.0.1 example.com\n"))));
    vertx.deployVerticle(new Client()).await();
  }

  private HttpClient client;

  @Override
  public Future<?> start() throws Exception {

    // Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

    HttpClientConfig config = new HttpClientConfig()
      .setSsl(true)
      .setFollowAlternativeServices(true)
      .setVerifyHost(false)
      .setVersions(HttpVersion.HTTP_1_1, HttpVersion.HTTP_3);

    client = vertx.createHttpClient(config, new ClientSSLOptions().setTrustAll(true));

    vertx.setPeriodic(1000, id -> {
      client.
        request(HttpMethod.GET, 8443, "example.com", "/")
        .compose(req -> req.send()
          .compose(resp -> {
            System.out.println("Got response " + resp.statusCode());
            return resp.body();
          }))
        .onSuccess(body -> System.out.println("Got data " + body.toString("ISO-8859-1")))
        .onFailure(err -> System.out.println("Request failed " + err.getMessage()));
    });

    return Future.succeededFuture();
  }
}

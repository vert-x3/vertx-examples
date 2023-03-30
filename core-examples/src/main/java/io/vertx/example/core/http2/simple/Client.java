package io.vertx.example.core.http2.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 * @author linghengqian
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    // Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

    HttpClientOptions options = new HttpClientOptions().
      setSsl(true).
      setUseAlpn(true).
      setProtocolVersion(HttpVersion.HTTP_2).
      setTrustAll(true).
      setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("server-key.pem").setCertPath("server-cert.pem"));
    HttpClient client = vertx.createHttpClient(options);
    client.request(HttpMethod.GET, 8443, "localhost", "/")
      .compose(req -> req.send()
        .compose(resp -> {
          System.out.println("Got response " + resp.statusCode());
          return resp.body();
        })).onSuccess(body -> {
        System.out.println("Got data " + body.toString("ISO-8859-1"));
      }).onFailure(Throwable::printStackTrace);
  }
}

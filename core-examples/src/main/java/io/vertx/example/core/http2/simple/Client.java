package io.vertx.example.core.http2.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.OpenSSLEngineOptions;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
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
        setOpenSslEngineOptions(new OpenSSLEngineOptions()).
        setProtocolVersion(HttpVersion.HTTP_2).
        setTrustAll(true);

    vertx.createHttpClient(options
    ).getNow(8443, "localhost", "/", resp -> {
      System.out.println("Got response " + resp.statusCode() + " with protocol " + resp.version());
      resp.bodyHandler(body -> System.out.println("Got data " + body.toString("ISO-8859-1")));
    });
  }
}

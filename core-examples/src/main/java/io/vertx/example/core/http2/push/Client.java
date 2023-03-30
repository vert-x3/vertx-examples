package io.vertx.example.core.http2.push;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.*;
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

    client.request(HttpMethod.GET, 8443, "localhost", "/").compose(request -> {

      // Set handler for server side push
      request.pushHandler(pushedReq -> {
        System.out.println("Receiving pushed content");
        pushedReq.response().compose(HttpClientResponse::body).onSuccess(body -> {
          System.out.println("Got pushed data " + body.toString("ISO-8859-1"));
        });
      });

      return request.send().compose(resp -> {
        System.out.println("Got response " + resp.statusCode() + " with protocol " + resp.version());
        return resp.body();
      });
    }).onSuccess(body -> {
      System.out.println("Got data " + body.toString("ISO-8859-1"));
    }).onFailure(Throwable::printStackTrace);
  }
}

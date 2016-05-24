package io.vertx.example.core.http.https;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;
import io.vertx.example.util.Runner;

/**
 * connect to a https server using a http CONNECT proxy.
 *
 * for this example to work, you need a http proxy (e.g. squid)
 * running on localhost:3128
 *
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class ClientWithProxy extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(ClientWithProxy.class);
  }

  @Override
  public void start() throws Exception {
    HttpClientOptions options = new HttpClientOptions().setSsl(true)
        .setProxyOptions(new ProxyOptions().setProxyType(ProxyType.HTTP).setProxyHost("localhost").setProxyPort(3128));
    vertx.createHttpClient(options).getNow(443, "en.wikipedia.org", "/", resp -> {
      System.out.println("Got response " + resp.statusCode());
      resp.bodyHandler(body -> System.out.println("Got data " + body.toString("UTF-8")));
    });
  }
}

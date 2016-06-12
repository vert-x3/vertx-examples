package io.vertx.example.core.http.https;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;
import io.vertx.example.util.Runner;

/**
 * Connect to a https server using a http CONNECT proxy.
 *
 * for this example to work, you need a http proxy (e.g. squid) running on localhost:3128
 *
 * To install Squid in
 *
 * - Linux: most Linux distributions include squid as package (e.g. Ubuntu apt-get install squid)
 *
 * - Mac: squid can be installed with homebrew
 *
 * - Windows: squid is available as compiled binary from http://wiki.squid-cache.org/SquidFaq/BinaryPackages#Windows as
 * normal windows binary or as cygwin package
 *
 * For testing, a CONNECT proxy is available in the core tests jar as {@link io.vertx.test.core.ConnectHttpProxy} that
 * can be started as part of a unit test
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
        .setProxyOptions(new ProxyOptions().setType(ProxyType.HTTP).setHost("localhost").setPort(3128));
    vertx.createHttpClient(options).getNow(443, "en.wikipedia.org", "/", resp -> {
      System.out.println("Got response " + resp.statusCode());
      resp.bodyHandler(body -> System.out.println("Got data " + body.toString("UTF-8")));
    });
  }
}

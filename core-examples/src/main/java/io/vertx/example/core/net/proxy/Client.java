package io.vertx.example.core.net.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;
import io.vertx.example.util.Runner;

/**
 * Connect to a tcp server using a socks proxy.
 *
 * for this example to work, you need a socks5 proxy running on localhost:1080
 *
 * To install a SOCKS5 proxy in
 *
 * - Linux: you can use Dante (https://www.inet.no/dante/), most Linux distributions include dante-server as package
 *
 * - Mac: dante can be installed with homebrew
 *
 * - Windows: unknown if dante is supported
 *
 * as alternative, you can use ssh as SOCKS5 server, it supports port forwarding with a "dynamic" port, which is the
 * connect method of SOCKS5
 *
 * {@code ssh -D 127.0.0.1:1080 host}
 * 
 * the same is possible with Putty when using Windows (define a Dynamic tunnel from port 1080 in Options / Connection /
 * SSH / Tunnels)
 *
 * For testing, a SOCKS5 proxy is available in the core tests jar as {@link io.vertx.test.core.SocksProxy} that can be
 * started as part of a unit test
 * 
 * @author <a href="http://oss.lehmann.cx/">Alexander Lehmann</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {
    NetClientOptions options = new NetClientOptions().setProxyOptions(
        new ProxyOptions().setType(ProxyType.SOCKS5).setHost("localhost").setPort(1080));
    vertx.createNetClient(options).connect(23, "aardmud.org", res -> {
      if (res.succeeded()) {
        NetSocket socket = res.result();
        socket.handler(buffer -> {
          System.out.print(buffer.toString("UTF-8"));
        });

      } else {
        System.out.println("Failed to connect " + res.cause());
      }
    });
  }
}

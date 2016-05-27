package io.vertx.example.core.net.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;
import io.vertx.example.util.Runner;

/**
 * connect to a tcp server using a socks proxy.
 *
 * for this example to work, you need a socks5 proxy running on localhost:1080
 * (you can use a dynamic port tunnel with ssh for example)
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

package io.vertx.example.core.http.proxyconnect;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Proxy extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Proxy.class);
  }

  @Override
  public void start() throws Exception {

    NetClient client = vertx.createNetClient(new NetClientOptions());

    vertx.createHttpServer().requestHandler(req -> {
      if (req.method() == HttpMethod.CONNECT) {

        // Determine proxied server address
        String proxyAddress = req.uri();
        int idx = proxyAddress.indexOf(':');
        String host = proxyAddress.substring(0, idx);
        int port = Integer.parseInt(proxyAddress.substring(idx + 1));

        System.out.println("Connecting to proxy " + proxyAddress);
        client.connect(port, host, ar -> {

          if (ar.succeeded()) {
            System.out.println("Connected to proxy");
            NetSocket clientSocket = req.netSocket();
            clientSocket.write("HTTP/1.0 200 Connection established\n\n");
            NetSocket serverSocket = ar.result();

            serverSocket.handler(buff -> {
              System.out.println("Forwarding server packet to the client");
              clientSocket.write(buff);
            });
            serverSocket.closeHandler(v -> {
              System.out.println("Server socket closed");
              clientSocket.close();
            });

            clientSocket.handler(buff -> {
              System.out.println("Forwarding client packet to the server");
              serverSocket.write(buff);
            });
            clientSocket.closeHandler(v -> {
              System.out.println("Client socket closed");
              serverSocket.close();
            });
          } else {

            System.out.println("Fail proxy connection");
            req.response().setStatusCode(403).end();
          }
        });
      } else {
        req.response().setStatusCode(405).end();
      }
    }).listen(8080);
  }
}

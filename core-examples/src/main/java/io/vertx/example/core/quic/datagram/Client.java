package io.vertx.example.core.quic.datagram;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.*;
import io.vertx.launcher.application.VertxApplication;

import java.util.List;

public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private QuicClient client;

  @Override
  public Future<?> start() throws Exception {

    // Enable datagram extension
    QuicClientConfig config = new QuicClientConfig();
    config
      .getTransportConfig()
      .setDatagramConfig(new QuicDatagramConfig().setEnabled(true));

    ClientSSLOptions sslOptions = new ClientSSLOptions()
      .setApplicationLayerProtocols(List.of("my-protocol"))
      .setTrustAll(true)
      .setHostnameVerificationAlgorithm("");

    client = vertx.createQuicClient(sslOptions);

    client
      .connect(1234, "localhost")
      .onComplete(res -> {
        if (res.succeeded()) {
          QuicConnection connection = res.result();

          if (connection.maxDatagramLength() == 0) {
            System.out.println("datagram extension disabled");
            return;
          }

          connection.datagramHandler(buff -> {
            System.out.println("client receiving " + buff.toString("UTF-8"));
          });

          // Now send some data
          for (int i = 0; i < 10; i++) {
            String str = "hello " + i + "\n";
            System.out.println("Quic client sending: " + str);
            connection.writeDatagram(Buffer.buffer(str));
          }
        } else {
          System.out.println("Failed to connect " + res.cause());
        }
      });

    return super.start();
  }
}

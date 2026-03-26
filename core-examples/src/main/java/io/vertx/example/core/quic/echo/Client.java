package io.vertx.example.core.quic.echo;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.net.ClientSSLOptions;
import io.vertx.core.net.QuicClient;
import io.vertx.core.net.QuicConnection;
import io.vertx.core.net.QuicStream;
import io.vertx.launcher.application.VertxApplication;

import java.util.List;

public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private QuicClient client;

  @Override
  public Future<?> start() throws Exception {

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

          for (int i = 0; i < 5;i++) {

            int idx = i;

            connection.openStream().onComplete(ar2 -> {

              System.out.println("Opened stream " + idx);

              if (ar2.succeeded()) {
                QuicStream stream = ar2.result();
                stream.handler(buff -> {
                  System.out.println("Client stream " + idx + " receiving " + buff.toString("UTF-8"));
                });

                // Now send some data
                for (int j = 0;j < 10; j++) {
                  String str = "hello " + j + "\n";
                  System.out.println("Quic client stream " + idx + " sending: " + str);
                  stream.write(str);
                }
              } else {
                System.out.println("Failed to open stream " + ar2.cause());
              }
            });
          }

        } else {
          System.out.println("Failed to connect " + res.cause());
        }
      });

    return super.start();
  }
}

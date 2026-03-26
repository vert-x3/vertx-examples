package io.vertx.example.core.quic.uni;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
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

    // Configure QUIC so the client can accept unidirectional streams
    QuicClientConfig config = new QuicClientConfig();
    config.getTransportConfig()
      .setInitialMaxStreamDataUni(1_048_576L)
      .setInitialMaxStreamsUni(100);

    ClientSSLOptions sslOptions = new ClientSSLOptions()
      .setApplicationLayerProtocols(List.of("my-protocol"))
      .setTrustAll(true)
      .setHostnameVerificationAlgorithm("");

    client = vertx.createQuicClient(config, sslOptions);

    client
      .connect(1234, "localhost")
      .onComplete(res -> {
        if (res.succeeded()) {
          QuicConnection connection = res.result();
          connection.streamHandler(stream -> {
            stream.handler(buff -> {
              System.out.println("client receiving " + buff.toString("UTF-8"));
            });
          });
        } else {
          System.out.println("Failed to connect " + res.cause());
        }
      });

    return super.start();
  }
}

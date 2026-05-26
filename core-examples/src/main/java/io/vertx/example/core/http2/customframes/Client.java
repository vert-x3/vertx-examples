package io.vertx.example.core.http2.customframes;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.core.net.ClientSSLOptions;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private HttpClient client;

  @Override
  public Future<?> start() throws Exception {

    // Note! in real-life you wouldn't often set trust all to true as it could leave you open to man in the middle attacks.

    HttpClientConfig config = new HttpClientConfig().
      setSsl(true).
      setVersions(HttpVersion.HTTP_2);

    client = vertx.createHttpClient(config, new ClientSSLOptions().setTrustAll(true));

    client.request(HttpMethod.GET, 8443, "localhost", "/")
      .onSuccess(request -> {
        request.response().onSuccess(resp -> {

          // Print custom frames received from server
          resp.customFrameHandler(frame -> {
            System.out.println("Got frame from server " + frame.payload().toString("UTF-8"));
          });
        });
        request.sendHead().onSuccess(v -> {

          // Once head has been sent we can send custom frames
          vertx.setPeriodic(1000, timerID -> {

            System.out.println("Sending ping frame to server");
            request.writeCustomFrame(10, 0, Buffer.buffer("ping"));
          });
        });
      });

    return super.start();
  }
}

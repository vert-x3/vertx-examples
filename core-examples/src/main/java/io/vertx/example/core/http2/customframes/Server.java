package io.vertx.example.core.http2.customframes;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.PemKeyCertOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
  }

  @Override
  public void start() throws Exception {

    HttpServer server =
      vertx.createHttpServer(new HttpServerOptions().
        setUseAlpn(true).
        setSsl(true).
        setPemKeyCertOptions(new PemKeyCertOptions()
          .setKeyPath("io/vertx/example/core/http2/customframes/server-key.pem")
          .setCertPath("io/vertx/example/core/http2/customframes/server-cert.pem")
        ));

    server.requestHandler(req -> {
      HttpServerResponse resp = req.response();

      req.customFrameHandler(frame -> {
        System.out.println("Received client frame " + frame.payload().toString("UTF-8"));

        // Write the sam
        resp.writeCustomFrame(10, 0, Buffer.buffer("pong"));
      });
    }).listen(8443);
  }
}

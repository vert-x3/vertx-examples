package io.vertx.example.core.http2.customframes;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.OpenSSLEngineOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.example.util.Runner;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    HttpServer server =
      vertx.createHttpServer(new HttpServerOptions().
          setUseAlpn(true).
          setSslEngineOptions(new OpenSSLEngineOptions()).
          setSsl(true).
          setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("server-key.pem").setCertPath("server-cert.pem")
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

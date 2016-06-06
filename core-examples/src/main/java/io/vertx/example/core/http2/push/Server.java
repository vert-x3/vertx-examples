package io.vertx.example.core.http2.push;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
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
          setOpenSslEngineOptions(new OpenSSLEngineOptions()).
          setSsl(true).
          setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("server-key.pem").setCertPath("server-cert.pem")
      ));

    server.requestHandler(req -> {
      String path = req.path();
      HttpServerResponse resp = req.response();
      switch (path) {
        case "/":

          resp.push(HttpMethod.GET, "/script.js", ar -> {
            if (ar.succeeded()) {
              System.out.println("sending push");
              HttpServerResponse pushedResp = ar.result();
              pushedResp.exceptionHandler(err -> {

              });
              pushedResp.sendFile("script.js");
            } else {
              // Sometimes Safari forbids push : "Server push not allowed to opposite endpoint."
              System.out.println(ar.cause().getMessage());;
            }
          });

          resp.sendFile("index.html");
          break;
        case "/script.js":
          resp.sendFile("script.js");
          break;
        default:
          System.out.println("Not found " + path);
          resp.setStatusCode(404).end();
          break;
      }
    });

    server.listen(8443, "localhost", ar -> {
      if (ar.succeeded()) {
        System.out.println("Server started");
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}

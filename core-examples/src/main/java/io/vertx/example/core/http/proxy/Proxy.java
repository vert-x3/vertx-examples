package io.vertx.example.core.http.proxy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
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
    HttpClient client = vertx.createHttpClient(new HttpClientOptions());
    vertx.createHttpServer().requestHandler(req -> {
      System.out.println("Proxying request: " + req.uri());
      client.request(req.method(), 8282, "localhost", req.uri())
        .onSuccess(c_req -> {
          c_req.setChunked(true);
          c_req.headers().setAll(req.headers());
          c_req.send(req).onSuccess(c_res -> {
            System.out.println("Proxying response: " + c_res.statusCode());
            req.response().setChunked(true);
            req.response().setStatusCode(c_res.statusCode());
            req.response().headers().setAll(c_res.headers());
            c_res.handler(data -> {
              System.out.println("Proxying response body: " + data.toString("ISO-8859-1"));
              req.response().write(data);
            });
            c_res.endHandler((v) -> req.response().end());
          }).onFailure(err -> {
            System.out.println("Back end failure");
            req.response().setStatusCode(500).end();
          });
      }).onFailure(err -> {
        System.out.println("Could not connect to localhost");
        req.response().setStatusCode(500).end();
      });
    }).listen(8080);
  }
}

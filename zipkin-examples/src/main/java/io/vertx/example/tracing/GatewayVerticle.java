package io.vertx.example.tracing;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClient;

import static io.vertx.core.http.HttpResponseExpectation.SC_OK;

public class GatewayVerticle extends AbstractVerticle {

  @Override
  public void start() {
    WebClient client = WebClient.create(vertx);
    vertx.createHttpServer().requestHandler(req -> {
      switch (req.path()) {
        case "/hello":
          client.get(8081, "localhost", "/")
            .send()
            .expecting(SC_OK)
            .onSuccess(resp -> req.response().end(resp.body()))
            .onFailure(failure -> {
              failure.printStackTrace();
              req.response().setStatusCode(500).end();
            });
          break;
        case "/joke":
          client.get(8082, "localhost", "/")
            .send()
            .expecting(SC_OK)
            .onSuccess(resp -> req.response().end(resp.body()))
            .onFailure(failure -> {
              failure.printStackTrace();
              req.response().setStatusCode(500).end();
            });
          break;
        default:
          req.response().setStatusCode(404).end();
          break;
      }
    }).listen(8080);
  }
}

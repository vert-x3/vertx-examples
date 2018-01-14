package io.vertx.example.webclient.response.clienterrors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("Content/type", "application/json")
        .setStatusCode(404)
        .end(new JsonObject()
          .put("message", "Not Found")
          .put("documentation_url", "https://developer.github.com/v3/users/#get-a-single-user")
          .encode()
        );

    }).listen(8080, listenResult -> {
      if (listenResult.failed()) {
        System.out.println("Could not start HTTP server");
        listenResult.cause().printStackTrace();
      } else {
        System.out.println("Server started");
      }
    });
  }
}

package io.vertx.example.webclient.response.jsonpojo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  public static class User {
    public String firstName;
    public String lastName;
    public boolean male;
  }

  @Override
  public void start() throws Exception {

    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("Content/type", "application/json")
        .end(new JsonObject()
          .put("firstName", "Dale")
          .put("lastName", "Cooper")
          .put("male", true)
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

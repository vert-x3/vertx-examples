package io.vertx.example.webclient.response.jsonobject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Server.class.getName());
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

package io.vertx.example.webclient.response.jsonpojo;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.json.JsonObject;
import io.vertx.launcher.application.VertxApplication;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
    System.out.println("Server started");
  }

  public static class User {
    public String firstName;
    public String lastName;
    public boolean male;
  }

  @Override
  public Future<?> start() throws Exception {

    return vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("Content/type", "application/json")
        .end(new JsonObject()
          .put("firstName", "Dale")
          .put("lastName", "Cooper")
          .put("male", true)
          .encode()
        );

    }).listen(8080);
  }
}

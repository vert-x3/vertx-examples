package io.vertx.example.rx.http.compose;

import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();
    server.requestStream().toObservable().subscribe(req -> {
      req.response().putHeader("content-type", "application/json").end(new JsonObject().put("time", System.currentTimeMillis()).toString());
    });
    server.listen(8080);
  }
}

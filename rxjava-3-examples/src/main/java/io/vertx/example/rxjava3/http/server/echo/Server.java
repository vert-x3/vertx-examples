package io.vertx.example.rxjava3.http.server.echo;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.core.http.HttpServerResponse;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public Completable rxStart() {
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(req -> {
      HttpServerResponse resp = req.response();
      String contentType = req.getHeader("Content-Type");
      if (contentType != null) {
        resp.putHeader("Content-Type", contentType);
      }
      resp.setChunked(true);
      req.toFlowable().subscribe(
        resp::write,
        err -> {
        },
        resp::end
      );
    });
    return server.listen(8080).ignoreElement();
  }
}

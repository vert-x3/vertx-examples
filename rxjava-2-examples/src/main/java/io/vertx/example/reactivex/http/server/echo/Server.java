package io.vertx.example.reactivex.http.server.echo;

import io.vertx.launcher.application.VertxApplication;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public void start() throws Exception {
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
    server.listen(8080);
  }
}

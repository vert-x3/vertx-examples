package io.vertx.example.reactivex.web.realtime;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Server.class.getName()});
  }

  @Override
  public Completable rxStart() {

    Router router = Router.router(vertx);

    router.route("/news-feed*").subRouter(SockJSHandler.create(vertx).socketHandler(sockJSSocket -> {

      // Consumer the event bus address as an Flowable
      Flowable<String> msg = vertx.eventBus().<String>consumer("news-feed")
        .bodyStream()
        .toFlowable();

      // Send the event to the client
      Disposable subscription = msg.subscribe(sockJSSocket::write);

      // Unsubscribe (dispose) when the socket closes
      sockJSSocket.endHandler(v -> {
        subscription.dispose();
      });
    }));

    // Serve the static resources
    router.route().handler(StaticHandler.create());

    HttpServer server = vertx.createHttpServer().requestHandler(router);

    // Publish a message to the address "news-feed" every second
    vertx.setPeriodic(1000, t -> vertx.eventBus().publish("news-feed", "news from the server!"));

    return server
      .rxListen(8080)
      .ignoreElement();
  }
}

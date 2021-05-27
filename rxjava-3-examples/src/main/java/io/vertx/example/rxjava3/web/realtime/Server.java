package io.vertx.example.rxjava3.web.realtime;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.vertx.example.util.Runner;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.handler.StaticHandler;
import io.vertx.rxjava3.ext.web.handler.sockjs.SockJSHandler;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    router.mountSubRouter("/news-feed/*", SockJSHandler.create(vertx).socketHandler(sockJSSocket -> {

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

    vertx.createHttpServer().requestHandler(router).listen(8080);

    // Publish a message to the address "news-feed" every second
    vertx.setPeriodic(1000, t -> vertx.eventBus().publish("news-feed", "news from the server!"));
  }
}

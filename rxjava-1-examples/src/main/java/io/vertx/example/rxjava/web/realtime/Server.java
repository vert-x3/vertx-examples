package io.vertx.example.rxjava.web.realtime;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.StaticHandler;
import io.vertx.rxjava.ext.web.handler.sockjs.SockJSHandler;
import rx.Observable;
import rx.Subscription;

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

    router.route("/news-feed/*").handler(SockJSHandler.create(vertx).socketHandler(sockJSSocket -> {

      // Consumer the event bus address as an Observable
      Observable<String> msg = vertx.eventBus().<String>consumer("news-feed")
        .bodyStream()
        .toObservable();

      // Send the event to the client
      Subscription subscription = msg.subscribe(sockJSSocket::write);

      // Unsubscribe when the socket closes
      sockJSSocket.endHandler(v -> {
        subscription.unsubscribe();
      });
    }));

    // Serve the static resources
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router).listen(8080);

    // Publish a message to the address "news-feed" every second
    vertx.setPeriodic(1000, t -> vertx.eventBus().publish("news-feed", "news from the server!"));
  }
}

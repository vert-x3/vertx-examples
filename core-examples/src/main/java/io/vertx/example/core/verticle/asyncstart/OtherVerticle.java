package io.vertx.example.core.verticle.asyncstart;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class OtherVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) throws Exception {

    System.out.println("In OtherVerticle.start (async)");

    // This verticle takes some time to start (maybe it has to deploy other verticles or whatever)
    // So we override the async version of start(), then we can mark the verticle as started some time later
    // when all the slow startup is done, without blocking the actual start method.

    // We simulate this long startup time by setting a timer
    vertx.setTimer(2000, tid -> {

      // Now everything is started, we can tell Vert.x this verticle is started then it will call the deploy handler
      // of the caller that originally deployed it

      System.out.println("Startup tasks are now complete, OtherVerticle is now started!");

      startFuture.complete();

    });

  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {

    // If you have slow cleanup tasks to perform, you can similarly override the async stop method

    vertx.setTimer(2000, tid -> {

      System.out.println("Cleanup tasks are now complete, OtherVerticle is now stopped!");

      stopFuture.complete();

    });

  }
}

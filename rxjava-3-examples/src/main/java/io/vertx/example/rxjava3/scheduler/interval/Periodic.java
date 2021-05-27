package io.vertx.example.rxjava3.scheduler.interval;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.vertx.example.util.Runner;
import io.vertx.rxjava3.core.AbstractVerticle;

import java.util.concurrent.TimeUnit;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Periodic extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Periodic.class);
  }

  @Override
  public void start() throws Exception {

    //
    Scheduler scheduler = io.vertx.rxjava3.core.RxHelper.scheduler(vertx);

    // Create a periodic event stream using Vertx scheduler
    Flowable<Long> o = Flowable.interval(1000, TimeUnit.MILLISECONDS, scheduler);

    o.subscribe(item -> {
      System.out.println("Got item " + item + " on thread " + Thread.currentThread().getName());
    });
  }
}

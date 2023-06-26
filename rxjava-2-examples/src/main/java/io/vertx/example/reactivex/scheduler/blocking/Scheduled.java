package io.vertx.example.reactivex.scheduler.blocking;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.vertx.core.Launcher;
import io.vertx.reactivex.core.AbstractVerticle;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Scheduled extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Scheduled.class.getName());
  }

  private String blockingLoad(String id) {

    // Simulate a blocking action
    try {
      Thread.sleep(100);
      return "someData for " + id + " on thread " + Thread.currentThread().getName();
    } catch (InterruptedException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void start() throws Exception {

    Flowable<String> o = Flowable.just("someID1", "someID2", "someID3", "someID4");

    // This scheduler can execute blocking actions
    Scheduler scheduler = io.vertx.reactivex.core.RxHelper.blockingScheduler(vertx);

    // All operations done on the observer now can be blocking
    o = o.observeOn(scheduler);

    // Load from a blocking api
    o = o.map(this::blockingLoad
    );

    o.subscribe(item -> {
      System.out.println("Got item " + item);
    }, Throwable::printStackTrace, () -> {
      System.out.println("Done");
    });
  }
}

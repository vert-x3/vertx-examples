package io.vertx.example.core.verticle.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;

/**
 * An example illustrating how worker verticles can be deployed and how to interact with them.
 *
 * This example prints the name of the current thread at various locations to exhibit the event loop <-> worker
 * thread switches.
 */
public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", MainVerticle.class.getName());
  }

  @Override
  public void start() throws Exception {
    System.out.println("[Main] Running in " + Thread.currentThread().getName());
    vertx
      .deployVerticle("io.vertx.example.core.verticle.worker.WorkerVerticle",
        new DeploymentOptions().setWorker(true));

    vertx.eventBus().request(
      "sample.data",
      "hello vert.x",
      r -> {
        System.out.println("[Main] Receiving reply ' " + r.result().body()
          + "' in " + Thread.currentThread().getName());
      }
    );
  }
}

package io.vertx.example.core.future;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.example.util.Runner;

import java.util.function.Function;

/**
 * An example showing how to use {@link io.vertx.core.Future#compose(Function)}
 */
public class ComposeExample extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(ComposeExample.class);
  }


  @Override
  public void start() throws Exception {
    Future<String> future = anAsyncAction();
    future.compose(this::anotherAsyncAction)
      .setHandler(ar -> {
        if (ar.failed()) {
          System.out.println("Something bad happened");
          ar.cause().printStackTrace();
        } else {
          System.out.println("Result: " + ar.result());
        }
      });
  }

  private Future<String> anAsyncAction() {
    Future<String> future = Future.future();
    // mimic something that take times
    vertx.setTimer(100, l -> future.complete("world"));
    return future;
  }

  private Future<String> anotherAsyncAction(String name) {
    Future<String> future = Future.future();
    // mimic something that take times
    vertx.setTimer(100, l -> future.complete("hello " + name));
    return future;
  }


}

package io.vertx.example.circuit.breaker;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * @author <a href="pahan.224@gmail.com">Pahan</a>
 */

public class Client extends AbstractVerticle {

  public static void main(String[] args)
  {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(Client.class.getName());
  }

  @Override
  public void start()
  {
    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
      new CircuitBreakerOptions().setMaxFailures(5).setTimeout(5000)
    ).openHandler(v -> {
      System.out.println("Circuit opened");
    }).closeHandler(v -> {
      System.out.println("Circuit closed");
    });

    breaker.executeWithFallback(
      future -> {
        vertx.createHttpClient().getNow(8080, "localhost", "/", response -> {
          if (response.statusCode() != 200) {
            future.fail("HTTP error");
          } else {
            response
              .exceptionHandler(future::fail)
              .bodyHandler(buffer -> {
                future.complete(buffer.toString());
              });
          }
        });
      }, v -> {
        // Executed when the circuit is opened
        return "Hello";
      })
      .setHandler(ar -> {
        // Do something with the result
      });
  }
}

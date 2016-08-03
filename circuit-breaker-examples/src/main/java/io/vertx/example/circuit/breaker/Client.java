package io.vertx.example.circuit.breaker;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;

public class Client extends AbstractVerticle {
  @Override
  public void start()
  {
    CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
      new CircuitBreakerOptions().setMaxFailures(5).setTimeout(50)
    ).openHandler(v -> {
      System.out.println("Circuit opened");
    }).closeHandler(v -> {
      System.out.println("Circuit closed");
    });

    breaker.execute(
      future -> {
        vertx.createHttpClient().getNow(8080, "localhost", "/", response -> {
          if (response.statusCode() != 200) {
            future.fail("HTTP error");
              } else {
                // Do something with the response
                future.complete();
              }
        });
      });
  }
}

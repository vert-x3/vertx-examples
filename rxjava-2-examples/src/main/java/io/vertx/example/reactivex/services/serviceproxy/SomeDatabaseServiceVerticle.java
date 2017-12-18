package io.vertx.example.reactivex.services.serviceproxy;


import io.vertx.example.util.Runner;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;

public class SomeDatabaseServiceVerticle extends AbstractVerticle {
  SomeDatabaseService someDatabaseService;

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runClusteredExample(SomeDatabaseServiceVerticle.class);
  }

  @Override
  public void start() throws Exception {
    // Use Factory method or just with constructor, either is OK
    someDatabaseService = SomeDatabaseService.create();

    // Register your service to the address.
    new ServiceBinder(vertx.getDelegate())
      .setAddress("proxy.address")
      .register(SomeDatabaseService.class, someDatabaseService);

  }
}

package io.vertx.example.reactivex.services.serviceproxy;


import io.vertx.core.Launcher;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.serviceproxy.ServiceBinder;

public class SomeDatabaseServiceVerticle extends AbstractVerticle {
  SomeDatabaseService someDatabaseService;

  public static void main(String[] args) {
    Launcher.executeCommand("run", SomeDatabaseServiceVerticle.class.getName(), "-cluster");
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

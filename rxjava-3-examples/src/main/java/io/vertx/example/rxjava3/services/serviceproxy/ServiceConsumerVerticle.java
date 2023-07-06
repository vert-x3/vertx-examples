package io.vertx.example.rxjava3.services.serviceproxy;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.core.AbstractVerticle;

public class ServiceConsumerVerticle extends AbstractVerticle {
  // In order to use Rx-ified methods, you need to declare the service in Rx-style.
  io.vertx.example.rxjava3.services.serviceproxy.rxjava3.SomeDatabaseService someDatabaseService;

  public static void main(String[] args) {
    Launcher.executeCommand("run", ServiceConsumerVerticle.class.getName(), "-cluster");
  }

  @Override
  public void start() throws Exception {
    someDatabaseService = SomeDatabaseService.createProxy(vertx.getDelegate(), "proxy.address");

    int id = 1;

    // Now you can use your Rx-ified methods.
    Single<JsonObject> single = someDatabaseService.rxGetDataById(id);

    single.subscribe(
      jsonObject -> System.out.println(jsonObject.encodePrettily()),
      throwable -> System.out.println(throwable.getMessage())
    );
  }
}

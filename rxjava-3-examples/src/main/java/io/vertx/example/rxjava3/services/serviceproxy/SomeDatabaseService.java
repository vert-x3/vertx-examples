package io.vertx.example.rxjava3.services.serviceproxy;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.example.rxjava3.services.serviceproxy.impl.SomeDatabaseServiceImpl;

@ProxyGen
@VertxGen
public interface SomeDatabaseService {
  @GenIgnore
  static SomeDatabaseService create() {
    return new SomeDatabaseServiceImpl();
  }

  @GenIgnore
  static io.vertx.example.rxjava3.services.serviceproxy.rxjava3.SomeDatabaseService createProxy(Vertx vertx, String address) {
    return new io.vertx.example.rxjava3.services.serviceproxy.rxjava3.SomeDatabaseService(new SomeDatabaseServiceVertxEBProxy(vertx, address));
  }

  // To use Rx-ified method, just declare your methods in a call-back style here.
  // The Rx-ified methods will be automatically generated.
  Future<JsonObject> getDataById(int id);
}

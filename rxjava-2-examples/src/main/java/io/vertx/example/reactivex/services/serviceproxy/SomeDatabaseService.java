package io.vertx.example.reactivex.services.serviceproxy;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.example.reactivex.services.serviceproxy.impl.SomeDatabaseServiceImpl;

@ProxyGen
@VertxGen
public interface SomeDatabaseService {
  @GenIgnore
  static SomeDatabaseService create() {
    return new SomeDatabaseServiceImpl();
  }

  @GenIgnore
  static io.vertx.example.reactivex.services.serviceproxy.reactivex.SomeDatabaseService createProxy(Vertx vertx, String address) {
    return new io.vertx.example.reactivex.services.serviceproxy.reactivex.SomeDatabaseService(new SomeDatabaseServiceVertxEBProxy(vertx, address));
  }

  // To use Rx-ified method, just declare your methods in a call-back style here.
  // The Rx-ified methods will be automatically generated.
  @Fluent
  SomeDatabaseService getDataById(int id, Handler<AsyncResult<JsonObject>> resultHandler);
}

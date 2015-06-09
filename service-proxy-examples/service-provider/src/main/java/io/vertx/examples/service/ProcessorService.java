package io.vertx.examples.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.examples.service.impl.ProcessorServiceImpl;

@ProxyGen
@VertxGen
public interface ProcessorService {

  // A couple of factory methods to create an instance and a proxy

  static ProcessorService create(Vertx vertx) {
    return new ProcessorServiceImpl();
  }

  static ProcessorService createProxy(Vertx vertx, String address) {
    // The name of the class to instantiate is the service interface + `VertxEBProxy`.
    // This class is generated during the compilation
    return new ProcessorServiceVertxEBProxy(vertx, address);
  }

  // The service methods

  void process(JsonObject document, Handler<AsyncResult<JsonObject>> resultHandler);

}

package io.vertx.examples.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.examples.service.impl.ProcessorServiceImpl;
import io.vertx.serviceproxy.ServiceProxyBuilder;

/**
 * The service interface.
 *
 * @author Lalit Rao
 */
@ProxyGen // Generate the proxy and handler
@VertxGen // Generate clients in non-java languages
public interface ProcessorService {

  int NO_NAME_ERROR = 2;
  int BAD_NAME_ERROR = 3;

  // A couple of factory methods to create an instance and a proxy
  static ProcessorService create(Vertx vertx) {
    return new ProcessorServiceImpl();
  }

  static ProcessorService createProxy(Vertx vertx, String address) {
    return new ServiceProxyBuilder(vertx)
      .setAddress(address)
      .build(ProcessorService.class);
    // Alternatively, you can create the proxy directly using:
    // return new ProcessorServiceVertxEBProxy(vertx, address);
    // The name of the class to instantiate is the service interface + `VertxEBProxy`.
    // This class is generated during the compilation
  }

  // The service methods
  Future<JsonObject> process(JsonObject document);

}

package io.vertx.example.web.proxies;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@ProxyGen
public interface MyService {

  @Fluent
  MyService sayHello(String name, Handler<AsyncResult<String>> handler);
}

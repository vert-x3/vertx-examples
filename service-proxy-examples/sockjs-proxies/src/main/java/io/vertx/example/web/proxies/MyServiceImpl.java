package io.vertx.example.web.proxies;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class MyServiceImpl implements MyService {
  @Override
  public MyService sayHello(String name, Handler<AsyncResult<String>> handler) {
    handler.handle(Future.succeededFuture("Hello " + name + "!"));
    return this;
  }
}

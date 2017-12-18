package io.vertx.example.reactivex.services.serviceproxy.impl;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService;

public class SomeDatabaseServiceImpl implements SomeDatabaseService {

  public SomeDatabaseServiceImpl() {
  }

  @Override
  public SomeDatabaseService getDataById(int id, Handler<AsyncResult<JsonObject>> resultHandler) {
    if (id > 0) {
      resultHandler.handle(Future.succeededFuture(new JsonObject()
        .put("id", id)
        .put("name", "vertx")));
    } else {
      resultHandler.handle(Future.failedFuture("Invalid id"));
    }
    return this;
  }
}

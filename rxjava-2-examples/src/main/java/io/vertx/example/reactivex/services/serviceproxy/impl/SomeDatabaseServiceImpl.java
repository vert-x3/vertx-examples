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
  public Future<JsonObject> getDataById(int id) {
    if (id > 0) {
      return Future.succeededFuture(new JsonObject()
        .put("id", id)
        .put("name", "vertx"));
    } else {
      return Future.failedFuture("Invalid id");
    }
  }
}

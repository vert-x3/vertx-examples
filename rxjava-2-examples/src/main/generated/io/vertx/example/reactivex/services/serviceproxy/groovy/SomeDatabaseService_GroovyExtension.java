package io.vertx.example.reactivex.services.serviceproxy.groovy;
public class SomeDatabaseService_GroovyExtension {
  public static io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService getDataById(io.vertx.example.reactivex.services.serviceproxy.SomeDatabaseService j_receiver, int id, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    io.vertx.core.impl.ConversionHelper.fromObject(j_receiver.getDataById(id,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.json.JsonObject>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.core.json.JsonObject> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.core.impl.ConversionHelper.fromJsonObject(event)));
      }
    } : null));
    return j_receiver;
  }
}

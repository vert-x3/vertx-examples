package io.vertx.examples.service.groovy;
public class ProcessorService_GroovyExtension {
  public static void process(io.vertx.examples.service.ProcessorService j_receiver, java.util.Map<String, Object> document, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.util.Map<String, Object>>> resultHandler) {
    j_receiver.process(document != null ? io.vertx.examples.service.groovy.internal.ConversionHelper.toJsonObject(document) : null,
      resultHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<io.vertx.core.json.JsonObject>>() {
      public void handle(io.vertx.core.AsyncResult<io.vertx.core.json.JsonObject> ar) {
        resultHandler.handle(ar.map(event -> io.vertx.examples.service.groovy.internal.ConversionHelper.fromJsonObject(event)));
      }
    } : null);
  }
}

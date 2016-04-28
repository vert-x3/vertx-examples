package io.vertx.examples.service.impl;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import io.vertx.examples.service.ProcessorService;

/**
 * Service implementation.
 * <p/>
 * Just add `approved` to the given document, unless its missing the
 * 'name' key, in which case an error is returned.
 */
public class ProcessorServiceImpl implements ProcessorService {

  @Override
  public void process(JsonObject document, Handler<AsyncResult<JsonObject>> resultHandler) {
    System.out.println("Processing...");
    JsonObject result = document.copy();
    if (!document.containsKey("name")) {
      resultHandler.handle(ServiceException.fail(NO_NAME_ERROR, "No name in the document"));
    } else if (document.getString("name").isEmpty()) {
      resultHandler.handle(ServiceException.fail(BAD_NAME_ERROR, "Bad name in the document: " +
        document.getString("name"), new JsonObject().put("name", document.getString("name"))));
    } else {
      result.put("approved", true);
      resultHandler.handle(Future.succeededFuture(result));
    }
  }

}

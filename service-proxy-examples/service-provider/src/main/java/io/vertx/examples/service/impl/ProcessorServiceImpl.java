package io.vertx.examples.service.impl;


import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.examples.service.ProcessorService;
import io.vertx.serviceproxy.ServiceException;

/**
 * Service implementation.
 * <p/>
 * Just add `approved` to the given document.
 */
public class ProcessorServiceImpl implements ProcessorService {

  @Override
  public Future<JsonObject> process(JsonObject document) {
    System.out.println("Processing...");
    JsonObject result = document.copy();
    if (!document.containsKey("name")) {
      return Future.failedFuture(new ServiceException(NO_NAME_ERROR, "No name in the document"));
    } else if (document.getString("name").isEmpty()  || document.getString("name").equalsIgnoreCase("bad")) {
      return Future.failedFuture(new ServiceException(BAD_NAME_ERROR, "Bad name in the document: " +
        document.getString("name"), new JsonObject().put("name", document.getString("name"))));
    } else {
      result.put("approved", true);
      return Future.succeededFuture(result);
    }
  }
}

package io.vertx.examples.service.consumer;

import io.vertx.serviceproxy.ServiceException;

import static io.vertx.examples.service.ProcessorService.BAD_NAME_ERROR;
import static io.vertx.examples.service.ProcessorService.NO_NAME_ERROR;

/**
 * Isolated failure management to support CodeTrans (generation of the example in the different languages)
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class Failures {
  public static void dealWithFailure(Throwable t) {
    if (t instanceof ServiceException) {
      ServiceException exc = (ServiceException) t;
      if (exc.failureCode() == BAD_NAME_ERROR) {
        System.out.println("Failed to process the document: The name in the document is bad. " +
            "The name provided is: " + exc.getDebugInfo().getString("name"));
      } else if (exc.failureCode() == NO_NAME_ERROR) {
        System.out.println("Failed to process the document: No name was found");
      }
    } else {
      System.out.println("Unexpected error: " + t);
    }
  }
}

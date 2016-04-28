package io.vertx.examples.service.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.examples.service.ProcessorService;
import io.vertx.examples.service.utils.Runner;
import io.vertx.serviceproxy.ServiceException;

import static io.vertx.examples.service.impl.ProcessorServiceImpl.BAD_NAME_ERROR;
import static io.vertx.examples.service.impl.ProcessorServiceImpl.NO_NAME_ERROR;


/**
 * A verticle consuming the provided {@link ProcessorService} service.
 */
public class ConsumerVerticle extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(ConsumerVerticle.class);
  }

  @Override
  public void start() throws Exception {
    ProcessorService service = ProcessorService.createProxy(vertx, "vertx.processor");

    JsonObject document = new JsonObject().put("name", "vertx");

    service.process(document, (r) -> {
      if (r.succeeded()) {
        System.out.println(r.result().encodePrettily());
      } else {
        if (r.cause() instanceof ServiceException) {
          ServiceException exc = (ServiceException) r.cause();
          if (exc.failureCode() == BAD_NAME_ERROR) {
            System.out.println("Failed to process the document: The name in the document is bad. " +
                "The name provided is: " + exc.getDebugInfo().getString("name"));
          } else if (exc.failureCode() == NO_NAME_ERROR) {
            System.out.println("Failed to process the document: No name was found");
          }
        } else {
          System.out.println("Unexpected error: " + r.cause());

        }
      }
    });
  }
}

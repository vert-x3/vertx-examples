package io.vertx.examples.service.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.examples.service.ProcessorService;
import io.vertx.examples.service.utils.Runner;


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
        Failures.dealWithFailure(r.cause());
      }
    });
  }
}

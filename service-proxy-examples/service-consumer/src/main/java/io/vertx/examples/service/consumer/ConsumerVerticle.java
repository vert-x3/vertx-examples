package io.vertx.examples.service.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.examples.service.ProcessorService;
import io.vertx.examples.service.ProcessorServiceVertxEBProxy;


public class ConsumerVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    new ProcessorServiceVertxEBProxy(vertx, "processor.mine");
    ProcessorService service = ProcessorService.createProxy(vertx, "vertx.processor");

    JsonObject document = new JsonObject().put("name", "vertx");

    service.process(document, (r) -> {
      if (r.succeeded()) {
        System.out.println(r.result());
      } else {
        System.out.println(r.cause().getMessage());
      }
    });

  }
}

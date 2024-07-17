package io.vertx.examples.service.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.examples.service.ProcessorService;
import io.vertx.launcher.application.VertxApplication;


/**
 * A verticle consuming the provided {@link ProcessorService} service.
 */
public class ConsumerVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ConsumerVerticle.class.getName(), "-cluster"});
  }

  @Override
  public void start() {
    ProcessorService service = ProcessorService.createProxy(vertx, "vertx.processor");

    JsonObject document = new JsonObject().put("name", "vertx");

    service.process(document)
      .onSuccess(json -> {
      System.out.println(json.encodePrettily());
    }).onFailure(failure -> {
      System.out.println(failure);
      Failures.dealWithFailure(failure.getCause());
    });
  }
}

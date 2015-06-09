package io.vertx.examples.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.service.impl.ProcessorServiceImpl;

/**
 * The verticle publishing the service.
 */
public class ProcessorServiceVerticle extends AbstractVerticle {

  ProcessorService service;

  @Override
  public void start() throws Exception {
    // Create the client object
    service = new ProcessorServiceImpl();

    new ProcessorServiceVertxProxyHandler(vertx, service, "vertx.processor").registerHandler();
  }

}

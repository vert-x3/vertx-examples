package io.vertx.examples.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.examples.service.impl.ProcessorServiceImpl;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * The verticle publishing the service.
 */
public class ProcessorServiceVerticle extends AbstractVerticle {

  ProcessorService service;

  @Override
  public void start() throws Exception {
    // Create the client object
    service = new ProcessorServiceImpl();
    // Register the handler
    ProxyHelper.registerService(ProcessorService.class, vertx, service, "vertx.processor");
  }

}

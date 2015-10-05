package io.vertx.examples.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.ExampleRunner;
import io.vertx.examples.service.impl.ProcessorServiceImpl;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * The verticle publishing the service.
 */
public class ProcessorServiceVerticle extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    ExampleRunner.runJavaExample("service-proxy-examples/service-provider/src/main/java/", ProcessorServiceVerticle.class, false);
  }

  ProcessorService service;

  @Override
  public void start() throws Exception {
    // Create the client object
    service = new ProcessorServiceImpl();
    // Register the handler
    ProxyHelper.registerService(ProcessorService.class, vertx, service, "vertx.processor");

    //
    Router router = Router.router(vertx);

    // Allow events for the designated addresses in/out of the event bus bridge
    BridgeOptions opts = new BridgeOptions()
        .addInboundPermitted(new PermittedOptions().setAddress("vertx.processor"))
        .addOutboundPermitted(new PermittedOptions().setAddress("vertx.processor"));

    // Create the event bus bridge and add it to the router.
    SockJSHandler ebHandler = SockJSHandler.create(vertx).bridge(opts);
    router.route("/eventbus/*").handler(ebHandler);

    //
    router.route().handler(StaticHandler.create());

    //
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);

  }

}

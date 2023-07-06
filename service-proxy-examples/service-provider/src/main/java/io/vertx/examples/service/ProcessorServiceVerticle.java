package io.vertx.examples.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.examples.service.impl.ProcessorServiceImpl;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.serviceproxy.ServiceBinder;

/**
 * The verticle publishing the service.
 *
 * @author Lalit Rao
 */
public class ProcessorServiceVerticle extends AbstractVerticle {

  MessageConsumer<JsonObject> messageConsumer;
  HttpServer httpServer;

  public static void main(String[] args) {
    Launcher.executeCommand("run", ProcessorServiceVerticle.class.getName(), "-cluster");
  }

  @Override
  public void start() {
    // Create the client object
    ProcessorService service = new ProcessorServiceImpl();
    // Register the handler
    new ServiceBinder(vertx)
      .setAddress("vertx.processor")
      .register(ProcessorService.class, service);

    Router router = Router.router(vertx);

    // Allow events for the designated addresses in/out of the event bus bridge
    SockJSBridgeOptions opts = new SockJSBridgeOptions()
      .addInboundPermitted(new PermittedOptions().setAddress("vertx.processor"))
      .addOutboundPermitted(new PermittedOptions().setAddress("vertx.processor"));

    // Create the event bus bridge and add it to the router.
    router.route("/eventbus*").subRouter(SockJSHandler.create(vertx).bridge(opts));
    router.route().handler(StaticHandler.create());

    //
    vertx.createHttpServer().requestHandler(router).listen(8080);

  }

}

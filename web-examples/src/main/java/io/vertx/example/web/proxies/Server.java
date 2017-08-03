package io.vertx.example.web.proxies;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.serviceproxy.ProxyHelper;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    // Create the client object
    MyService service = new MyServiceImpl();

    // Register the handler
    ProxyHelper.registerService(MyService.class, vertx, service, "proxy.example");

    Router router = Router.router(vertx);

    BridgeOptions options = new BridgeOptions().addInboundPermitted(new PermittedOptions().setAddress("proxy.example"));

    router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options));

    // Serve the static resources
    router.route().handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}

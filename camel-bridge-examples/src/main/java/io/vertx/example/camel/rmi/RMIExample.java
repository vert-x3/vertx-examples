package io.vertx.example.camel.rmi;

import io.vertx.camel.CamelBridge;
import io.vertx.camel.CamelBridgeOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static io.vertx.camel.OutboundMapping.fromVertx;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RMIExample extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(RMIExample.class.getName());
  }


  @Override
  public void start() throws Exception {
    ApplicationContext app = new ClassPathXmlApplicationContext("META-INF/spring/camelContext.xml");
    CamelContext camel = app.getBean("camel", CamelContext.class);

    CamelBridge.create(vertx, new CamelBridgeOptions(camel)
        .addOutboundMapping(fromVertx("invocation").toCamel("rmiService")))
        .start();

    vertx.createHttpServer()
        .requestHandler(this::invoke)
        .listen(8080);

  }

  private void invoke(HttpServerRequest request) {
    String param = request.getParam("name");
    if (param == null) {
      param = "vert.x";
    }
    vertx.eventBus().<String>send("invocation", param, reply -> {
      if (reply.failed()) {
        request.response().setStatusCode(400).end(reply.cause().getMessage());
      } else {
        request.response().setStatusCode(400).end(reply.result().body());
      }
    });
  }
}

package io.vertx.example.kafka.dashboard;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.kafka.client.consumer.KafkaReadStream;

import java.util.Collections;

public class DashboardVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {

    Router router = Router.router(vertx);

    // The event bus bridge handler
    BridgeOptions options = new BridgeOptions();
    options.setOutboundPermitted(Collections.singletonList(new PermittedOptions().setAddress("dashboard")));
    router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options));

    // The web server handler
    router.route().handler(StaticHandler.create().setCachingEnabled(false));

    // Start http server
    HttpServer httpServer = vertx.createHttpServer();
    httpServer.requestHandler(router).listen(8080, ar -> {
      if (ar.succeeded()) {
        System.out.println("Http server started");
      } else {
        ar.cause().printStackTrace();
      }
    });

    // Our dashboard that aggregates metrics from various kafka topics
    JsonObject dashboard = new JsonObject();

    // Publish the dashboard to the browser over the bus
    vertx.setPeriodic(1000, timerID -> {
      vertx.eventBus().publish("dashboard", dashboard);
    });

    // Get the Kafka consumer config
    JsonObject config = config();

    // Create the consumer
    KafkaReadStream<String, JsonObject> consumer = KafkaReadStream.create(vertx, config.getMap(), String.class, JsonObject.class);

    // Aggregates metrics in the dashboard
    consumer.handler(record -> {
      JsonObject obj = record.value();
      dashboard.mergeIn(obj);
    });

    // Subscribe to Kafka
    consumer.subscribe(Collections.singleton("the_topic"));
  }
}

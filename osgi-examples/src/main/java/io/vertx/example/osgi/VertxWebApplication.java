package io.vertx.example.osgi;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.apache.felix.ipojo.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static io.vertx.example.osgi.TcclSwitch.executeWithTCCLSwitch;

/**
 * A vert.x web application in OSGi.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@Component
@Provides
@Instantiate
public class VertxWebApplication extends AbstractVerticle {

  private final static Logger LOGGER = Logger.getLogger("VertxHttpServer");

  @Requires
  Vertx vertx;
  private HttpServer server;

  private Map<String, JsonObject> products = new HashMap<>();

  @Validate
  public void start() throws Exception {
    setUpInitialData();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/products/:productID").handler(this::handleGetProduct);
    router.put("/products/:productID").handler(this::handleAddProduct);
    router.get("/products").handler(this::handleListProducts);

    router.get("/assets/*").handler(StaticHandler.create("assets", this.getClass().getClassLoader()));

    LOGGER.info("Creating HTTP server for vert.x web application");
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(router::accept).listen(8081);
  }

  @Invalidate
  public void stop() {
    if (server != null) {
      server.close();
    }
  }

  private void handleGetProduct(RoutingContext routingContext) {
    String productID = routingContext.request().getParam("productID");
    HttpServerResponse response = routingContext.response();
    if (productID == null) {
      sendError(400, response);
    } else {
      JsonObject product = products.get(productID);
      if (product == null) {
        sendError(404, response);
      } else {
        response.putHeader("content-type", "application/json").end(product.encodePrettily());
      }
    }
  }

  private void handleAddProduct(RoutingContext routingContext) {
    String productID = routingContext.request().getParam("productID");
    HttpServerResponse response = routingContext.response();
    if (productID == null) {
      sendError(400, response);
    } else {
      JsonObject product = routingContext.getBodyAsJson();
      if (product == null) {
        sendError(400, response);
      } else {
        products.put(productID, product);
        response.end();
      }
    }
  }

  private void handleListProducts(RoutingContext routingContext) {
    JsonArray arr = new JsonArray();
    products.forEach((k, v) -> arr.add(v));
    routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

  private void setUpInitialData() {
    LOGGER.info("Setup initial data");
    addProduct(new JsonObject().put("id", "prod3568").put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
    addProduct(new JsonObject().put("id", "prod7340").put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
    addProduct(new JsonObject().put("id", "prod8643").put("name", "Spatula").put("price", 1.00).put("weight", 80));
  }

  private void addProduct(JsonObject product) {
    products.put(product.getString("id"), product);
  }

}

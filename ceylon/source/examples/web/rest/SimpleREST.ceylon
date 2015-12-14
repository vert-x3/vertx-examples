import io.vertx.ceylon.web { ... }
import io.vertx.ceylon.web.handler { ... }
import io.vertx.ceylon.core { Verticle }
import ceylon.json { JsonObject=Object, JsonArray=Array }
import ceylon.collection { HashMap }
import io.vertx.ceylon.core.http { HttpServerResponse }

shared class SimpleREST() extends Verticle() {
  
  shared actual void start() {    
    
    setUpInitialData();
    
    value router_ = router.router(vertx);

    router_.route().handler(bodyHandler.create().handle);
    router_.get("/products/:productID").handler(handleGetProduct);
    router_.put("/products/:productID").handler(handleAddProduct);
    router_.get("/products").handler(handleListProducts);
    
    vertx.createHttpServer().requestHandler(router_.accept).listen(8080);
  }

  void handleGetProduct(RoutingContext routingContext) {
    value productID = routingContext.request().getParam("productID");
    value response = routingContext.response();
    if (exists productID) {
      value product = products[productID];
      if (exists product) {
        response.putHeader("content-type", "application/json").end(product.string);
      } else {
        sendError(404, response);
      }
    } else {
      sendError(400, response);
    }
  }
  
  void handleAddProduct(RoutingContext routingContext) {
    value productID = routingContext.request().getParam("productID");
    value response = routingContext.response();
    if (exists productID) {
      value product = routingContext.getBodyAsJson();
      if (exists product) {
        products.put(productID, product);
        response.end();
      } else {
        sendError(400, response);
      }
    } else {
      sendError(400, response);
    }
  }
  
  void handleListProducts(RoutingContext routingContext) => 
    routingContext.response().putHeader("content-type", "application/json").end(JsonArray(products.items).string);

  void sendError(Integer statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }
}



void setUpInitialData() {
  addProduct(JsonObject { "id"->"prod3568", "name"->"Egg Whisk", "price"->3.99, "weight"->150 });
  addProduct(JsonObject { "id"->"prod7340", "name"->"Tea Cosy", "price"->5.99, "weight"->100 });
  addProduct(JsonObject { "id"->"prod8643", "name"->"Spatula", "price"->1.0, "weight"->80 });
}

HashMap<String, JsonObject> products = HashMap<String, JsonObject>();

void addProduct(JsonObject product) => products.put(product.getString("id"), product);
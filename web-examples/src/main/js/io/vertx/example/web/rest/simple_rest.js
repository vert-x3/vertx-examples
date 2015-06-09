var Router = require("vertx-web-js/router");
var BodyHandler = require("vertx-web-js/body_handler");
var products = {};
var addProduct = function(product) {
  products[product.id] = product;
};
var setUpInitialData = function() {
  addProduct({
    "id" : "prod3568",
    "name" : "Egg Whisk",
    "price" : 3.99,
    "weight" : 150
  });
  addProduct({
    "id" : "prod7340",
    "name" : "Tea Cosy",
    "price" : 5.99,
    "weight" : 100
  });
  addProduct({
    "id" : "prod8643",
    "name" : "Spatula",
    "price" : 1.0,
    "weight" : 80
  });
};
var handleGetProduct = function(routingContext) {
  var productID = routingContext.request().getParam("productID");
  var response = routingContext.response();
  if (productID === null) {
    sendError(400, response);
  } else {
    var product = products[productID];
    if (product === null) {
      sendError(404, response);
    } else {
      response.putHeader("content-type", "application/json").end(JSON.stringify(product));
    }
  }
};
var sendError = function(statusCode, response) {
  response.setStatusCode(statusCode).end();
};
var handleListProducts = function(routingContext) {
  var arr = [
  ];
  products.forEach(function (v, k) {
    arr.push(v);
  });
  routingContext.response().putHeader("content-type", "application/json").end(JSON.stringify(arr));
};
var handleAddProduct = function(routingContext) {
  var productID = routingContext.request().getParam("productID");
  var response = routingContext.response();
  if (productID === null) {
    sendError(400, response);
  } else {
    var product = routingContext.getBodyAsJson();
    if (product === null) {
      sendError(400, response);
    } else {
      products[productID] = product;
      response.end();
    }
  }
};

setUpInitialData();

var router = Router.router(vertx);

router.route().handler(BodyHandler.create().handle);
router.get("/products/:productID").handler(handleGetProduct);
router.put("/products/:productID").handler(handleAddProduct);
router.get("/products").handler(handleListProducts);

vertx.createHttpServer().requestHandler(router.accept).listen(8080);

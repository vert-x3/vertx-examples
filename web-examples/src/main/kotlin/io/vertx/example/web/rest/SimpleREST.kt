import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.common.json.*

class start : io.vertx.core.AbstractVerticle()  {
  var products = mutableMapOf<String, Any?>()
  fun addProduct(product: io.vertx.core.json.JsonObject) {
    products[product.getString("id")] = product
  }
  fun setUpInitialData() {
    addProduct(json {
      obj(
        "id" to "prod3568",
        "name" to "Egg Whisk",
        "price" to 3.99,
        "weight" to 150
      )
    })
    addProduct(json {
      obj(
        "id" to "prod7340",
        "name" to "Tea Cosy",
        "price" to 5.99,
        "weight" to 100
      )
    })
    addProduct(json {
      obj(
        "id" to "prod8643",
        "name" to "Spatula",
        "price" to 1.0,
        "weight" to 80
      )
    })
  }
  fun handleGetProduct(routingContext: RoutingContext) {
    var productID = routingContext.request().getParam("productID")
    var response = routingContext.response()
    if (productID == null) {
      sendError(400, response)
    } else {
      var product = products[productID]
      if (product == null) {
        sendError(404, response)
      } else {
        response.putHeader("content-type", "application/json").end(product.toString())
      }
    }
  }
  fun sendError(statusCode: int, response: HttpServerResponse) {
    response.setStatusCode(statusCode).end()
  }
  fun handleListProducts(routingContext: RoutingContext) {
    var arr = json {
      array()
    }
    for ((k, v) in products) {
      arr.add(v)
    }

    routingContext.response().putHeader("content-type", "application/json").end(arr.toString())
  }
  fun handleAddProduct(routingContext: RoutingContext) {
    var productID = routingContext.request().getParam("productID")
    var response = routingContext.response()
    if (productID == null) {
      sendError(400, response)
    } else {
      var product = routingContext.getBodyAsJson()
      if (product == null) {
        sendError(400, response)
      } else {
        products[productID] = product
        response.end()
      }
    }
  }
  override fun start() {

    setUpInitialData()

    var router = Router.router(vertx)

    router.route().handler(BodyHandler.create())
    router.get("/products/:productID").handler({ handleGetProduct(it) })
    router.put("/products/:productID").handler({ handleAddProduct(it) })
    router.get("/products").handler({ handleListProducts(it) })

    vertx.createHttpServer().requestHandler({ router.accept(it) }).listen(8080)
  }
}

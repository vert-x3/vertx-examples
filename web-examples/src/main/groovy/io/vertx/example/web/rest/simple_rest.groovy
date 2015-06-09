import groovy.transform.Field
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
@Field def products = [:]
def addProduct(product) {
  products[product.id] = product
}
def setUpInitialData() {
  this.addProduct([
    id:"prod3568",
    name:"Egg Whisk",
    price:3.99d,
    weight:150
  ])
  this.addProduct([
    id:"prod7340",
    name:"Tea Cosy",
    price:5.99d,
    weight:100
  ])
  this.addProduct([
    id:"prod8643",
    name:"Spatula",
    price:1.0d,
    weight:80
  ])
}
def handleGetProduct(routingContext) {
  def productID = routingContext.request().getParam("productID")
  def response = routingContext.response()
  if (productID == null) {
    this.sendError(400, response)
  } else {
    def product = products[productID]
    if (product == null) {
      this.sendError(404, response)
    } else {
      response.putHeader("content-type", "application/json").end(groovy.json.JsonOutput.toJson(product))
    }
  }
}
def sendError(statusCode, response) {
  response.setStatusCode(statusCode).end()
}
def handleListProducts(routingContext) {
  def arr = [
  ]
  products.each { k, v ->
    arr.add(v)
  }
  routingContext.response().putHeader("content-type", "application/json").end(groovy.json.JsonOutput.toJson(arr))
}
def handleAddProduct(routingContext) {
  def productID = routingContext.request().getParam("productID")
  def response = routingContext.response()
  if (productID == null) {
    this.sendError(400, response)
  } else {
    def product = routingContext.getBodyAsJson()
    if (product == null) {
      this.sendError(400, response)
    } else {
      products[productID] = product
      response.end()
    }
  }
}

this.setUpInitialData()

def router = Router.router(vertx)

router.route().handler(BodyHandler.create())
router.get("/products/:productID").handler(this.&handleGetProduct)
router.put("/products/:productID").handler(this.&handleAddProduct)
router.get("/products").handler(this.&handleListProducts)

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)

require 'json'
require 'vertx-web/router'
require 'vertx-web/body_handler'
products = Hash.new()
def add_product(product)
  products[product['id']] = product
end
def set_up_initial_data()
  add_product({
    'id' => "prod3568",
    'name' => "Egg Whisk",
    'price' => 3.99,
    'weight' => 150
  })
  add_product({
    'id' => "prod7340",
    'name' => "Tea Cosy",
    'price' => 5.99,
    'weight' => 100
  })
  add_product({
    'id' => "prod8643",
    'name' => "Spatula",
    'price' => 1.0,
    'weight' => 80
  })
end
def handle_get_product(routingContext)
  productID = routingContext.request().get_param("productID")
  response = routingContext.response()
  if (productID == nil)
    send_error(400, response)
  else
    product = products[productID]
    if (product == nil)
      send_error(404, response)
    else
      response.put_header("content-type", "application/json").end(JSON.generate(product))
    end
  end
end
def send_error(statusCode, response)
  response.set_status_code(statusCode).end()
end
def handle_list_products(routingContext)
  arr = [
  ]
  products.each_pair { |k,v|
    arr.push(v)
  }
  routingContext.response().put_header("content-type", "application/json").end(JSON.generate(arr))
end
def handle_add_product(routingContext)
  productID = routingContext.request().get_param("productID")
  response = routingContext.response()
  if (productID == nil)
    send_error(400, response)
  else
    product = routingContext.get_body_as_json()
    if (product == nil)
      send_error(400, response)
    else
      products[productID] = product
      response.end()
    end
  end
end

set_up_initial_data()

router = VertxWeb::Router.router($vertx)

router.route().handler(&VertxWeb::BodyHandler.create().method(:handle))
router.get("/products/:productID").handler(&method(:handle_get_product))
router.put("/products/:productID").handler(&method(:handle_add_product))
router.get("/products").handler(&method(:handle_list_products))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

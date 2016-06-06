require 'vertx-web/router'
require 'vertx-web/body_handler'

router = VertxWeb::Router.router($vertx)

# Enable multipart form data parsing
router.route().handler(&VertxWeb::BodyHandler.create().method(:handle))

router.route("/").handler() { |routingContext|
  routingContext.response().put_header("content-type", "text/html").end("<form action=\"/form\" method=\"post\">\n    <div>\n        <label for=\"name\">Enter your name:</label>\n        <input type=\"text\" id=\"name\" name=\"name\" />\n    </div>\n    <div class=\"button\">\n        <button type=\"submit\">Send</button>\n    </div></form>")
}

# handle the form
router.post("/form").handler() { |ctx|
  ctx.response().put_header(Java::IoVertxCoreHttp::HttpHeaders::CONTENT_TYPE, "text/plain")
  # note the form attribute matches the html form element name.
  ctx.response().end("Hello #{ctx.request().get_param("name")}!")
}

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

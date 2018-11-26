require 'vertx-web/router'
require 'vertx-web/body_handler'

router = VertxWeb::Router.router($vertx)

# Enable multipart form data parsing
router.route().handler(&VertxWeb::BodyHandler.create().set_uploads_directory("uploads").method(:handle))

router.route("/").handler() { |routingContext|
  routingContext.response().put_header("content-type", "text/html").end("<form action=\"/form\" method=\"post\" enctype=\"multipart/form-data\">\n    <div>\n        <label for=\"name\">Select a file:</label>\n        <input type=\"file\" name=\"file\" />\n    </div>\n    <div class=\"button\">\n        <button type=\"submit\">Send</button>\n    </div></form>")
}

# handle the form
router.post("/form").handler() { |ctx|
  ctx.response().put_header("Content-Type", "text/plain")

  ctx.response().set_chunked(true)

  ctx.file_uploads().each do |f|
    puts "f"
    ctx.response().write("Filename: #{f.file_name()}")
    ctx.response().write("\n")
    ctx.response().write("Size: #{f.size()}")
  end

  ctx.response().end()
}

$vertx.create_http_server().request_handler(&router.method(:handle)).listen(8080)

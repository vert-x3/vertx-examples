require 'vertx-web/router'
require 'vertx-web/cors_handler'
require 'vertx-web/static_handler'

router = VertxWeb::Router.router($vertx)

allowedHeaders = Java::JavaUtil::HashSet.new()
allowedHeaders.add?("x-requested-with")
allowedHeaders.add?("Access-Control-Allow-Origin")
allowedHeaders.add?("origin")
allowedHeaders.add?("Content-Type")
allowedHeaders.add?("accept")
allowedHeaders.add?("X-PINGARUNER")

allowedMethods = Java::JavaUtil::HashSet.new()
allowedMethods.add?(:GET)
allowedMethods.add?(:POST)
allowedMethods.add?(:OPTIONS)
=begin
 * these methods aren't necessary for this sample, 
 * but you may need them for your projects
 =end
allowedMethods.add?(:DELETE)
allowedMethods.add?(:PATCH)
allowedMethods.add?(:PUT)

router.route().handler(&VertxWeb::CorsHandler.create("*").allowed_headers(allowedHeaders).allowed_methods(allowedMethods).method(:handle))

router.get("/access-control-with-get").handler() { |ctx|
  httpServerResponse = ctx.response()
  httpServerResponse.set_chunked(true)
  headers = ctx.request().headers()
  headers.names().each do |key|
    httpServerResponse.write("#{key}: ")
    httpServerResponse.write(headers.get(key))
    httpServerResponse.write("<br>")
  end
  httpServerResponse.put_header("Content-Type", "application/text").end("Success")
}

router.post("/access-control-with-post-preflight").handler() { |ctx|
  httpServerResponse = ctx.response()
  httpServerResponse.set_chunked(true)
  headers = ctx.request().headers()
  headers.names().each do |key|
    httpServerResponse.write("#{key}: ")
    httpServerResponse.write(headers.get(key))
    httpServerResponse.write("<br>")
  end
  httpServerResponse.put_header("Content-Type", "application/text").end("Success")
}

# Serve the static resources
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:handle)).listen(8080)

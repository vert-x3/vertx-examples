require 'vertx-web/router'
require 'vertx-web/pebble_template_engine'

# To simplify the development of the web components we use a Router to route all HTTP requests
# to organize our code in a reusable way.
router = VertxWeb::Router.router($vertx)

# In order to use a template we first need to create an engine
engine = VertxWeb::PebbleTemplateEngine.create($vertx)

# Entry point to the application, this will render a custom template.
router.get().handler() { |ctx|
  # we define a hardcoded title for our application
  ctx.put("name", "Vert.x Web")

  # and now delegate to the engine to render it.
  engine.render(ctx, "templates/index.peb") { |res_err,res|
    if (res_err == nil)
      ctx.response().end(res)
    else
      ctx.fail(res_err)
    end
  }
}

# start a HTTP web server on port 8080
$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

require 'vertx-web/router'
require 'vertx-web/cookie_handler'
require 'vertx-web/cookie'
require 'vertx-web/static_handler'

router = VertxWeb::Router.router($vertx)

# This cookie handler will be called for all routes
router.route().handler(&VertxWeb::CookieHandler.create().method(:handle))

# on every path increment the counter
router.route().handler() { |ctx|
  someCookie = ctx.get_cookie("visits")

  visits = 0
  if (someCookie != nil)
    cookieValue = someCookie.get_value()
    begin
      visits = Java::JavaLang::Long.parse_long(cookieValue)
    rescue
      visits = 0
    end

  end

  # increment the tracking
  visits+=1

  # Add a cookie - this will get written back in the response automatically
  ctx.add_cookie(VertxWeb::Cookie.cookie("visits", "#{visits}"))

  ctx.next()
}

# Serve the static resources
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:handle)).listen(8080)

require 'vertx-web/router'
require 'vertx-web/cookie_handler'
require 'vertx-web/session_handler'
require 'vertx-web/local_session_store'
require 'vertx-web/body_handler'
require 'vertx-auth/shiro_auth'
require 'vertx-web/redirect_auth_handler'
require 'vertx-web/static_handler'
require 'vertx-web/form_login_handler'

router = VertxWeb::Router.router($vertx)

# We need cookies, sessions and request bodies
router.route().handler(&VertxWeb::CookieHandler.create().method(:handle))
router.route().handler(&VertxWeb::SessionHandler.create(VertxWeb::LocalSessionStore.create($vertx)).method(:handle))
router.route().handler(&VertxWeb::BodyHandler.create().method(:handle))

# Simple auth service which uses a properties file for user/role info
authProvider = VertxAuth::ShiroAuth.create($vertx, :PROPERTIES, {
})

# Any requests to URI starting '/private/' require login
router.route("/private/*").handler(&VertxWeb::RedirectAuthHandler.create(authProvider, "/loginpage.html").method(:handle))

# Serve the static private pages from directory 'private'
router.route("/private/*").handler(&VertxWeb::StaticHandler.create().set_caching_enabled(false).set_web_root("private").method(:handle))

# Handles the actual login
router.route("/loginhandler").handler(&VertxWeb::FormLoginHandler.create(authProvider).method(:handle))

# Implement logout
router.route("/logout").handler() { |context|
  context.set_user(nil)
  # Redirect back to the index page
  context.response().put_header("location", "/").set_status_code(302).end()
}

# Serve the non private static pages
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

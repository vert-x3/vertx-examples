require 'vertx-apex/router'
require 'vertx-apex/cookie_handler'
require 'vertx-apex/session_handler'
require 'vertx-apex/local_session_store'
require 'vertx-apex/body_handler'
require 'vertx-auth/shiro_auth_provider'
require 'vertx-apex/redirect_auth_handler'
require 'vertx-apex/static_handler'
require 'vertx-apex/form_login_handler'

router = VertxApex::Router.router($vertx)

# We need cookies, sessions and request bodies
router.route().handler(&VertxApex::CookieHandler.create().method(:handle))
router.route().handler(&VertxApex::SessionHandler.create(VertxApex::LocalSessionStore.create($vertx)).method(:handle))
router.route().handler(&VertxApex::BodyHandler.create().method(:handle))

# Simple auth service which uses a properties file for user/role info
authProvider = VertxAuth::ShiroAuthProvider.create($vertx, :PROPERTIES, {
})

# Any requests to URI starting '/private/' require login
router.route("/private/*").handler(&VertxApex::RedirectAuthHandler.create(authProvider, "/loginpage.html").method(:handle))

# Serve the static private pages from directory 'private'
router.route("/private/*").handler(&VertxApex::StaticHandler.create().set_caching_enabled(false).set_web_root("private").method(:handle))

# Handles the actual login
router.route("/loginhandler").handler(&VertxApex::FormLoginHandler.create(authProvider).method(:handle))

# Implement logout
router.route("/logout").handler() { |context|
  context.session().logout()
  # Redirect back to the index page
  context.response().put_header("location", "/").set_status_code(302).end()
}

# Serve the non private static pages
router.route().handler(&VertxApex::StaticHandler.create().method(:handle))

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

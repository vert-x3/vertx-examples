var Router = require("vertx-apex-js/router");
var CookieHandler = require("vertx-apex-js/cookie_handler");
var SessionHandler = require("vertx-apex-js/session_handler");
var LocalSessionStore = require("vertx-apex-js/local_session_store");
var BodyHandler = require("vertx-apex-js/body_handler");
var ShiroAuthService = require("vertx-auth-js/shiro_auth_service");
var RedirectAuthHandler = require("vertx-apex-js/redirect_auth_handler");
var StaticHandler = require("vertx-apex-js/static_handler");
var FormLoginHandler = require("vertx-apex-js/form_login_handler");

var router = Router.router(vertx);

// We need cookies, sessions and request bodies
router.route().handler(CookieHandler.create().handle);
router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).handle);
router.route().handler(BodyHandler.create().handle);

// Simple auth service which uses a properties file for user/role info
var authService = ShiroAuthService.create(vertx, 'PROPERTIES', {
});

// Any requests to URI starting '/private/' require login
router.route("/private/*").handler(RedirectAuthHandler.create(authService, "/loginpage.html").handle);

// Serve the static private pages from directory 'private'
router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private").handle);

// Handles the actual login
router.route("/loginhandler").handler(FormLoginHandler.create(authService).handle);

// Implement logout
router.route("/logout").handler(function (context) {
  context.session().logout();
  // Redirect back to the index page
  context.response().putHeader("location", "/").setStatusCode(302).end();
});

// Serve the non private static pages
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.accept).listen(8080);

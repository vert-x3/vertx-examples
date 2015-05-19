var Router = require("vertx-web-js/router");
var CookieHandler = require("vertx-web-js/cookie_handler");
var SessionHandler = require("vertx-web-js/session_handler");
var LocalSessionStore = require("vertx-web-js/local_session_store");
var BodyHandler = require("vertx-web-js/body_handler");
var ShiroAuth = require("vertx-auth-js/shiro_auth");
var RedirectAuthHandler = require("vertx-web-js/redirect_auth_handler");
var StaticHandler = require("vertx-web-js/static_handler");
var FormLoginHandler = require("vertx-web-js/form_login_handler");

var router = Router.router(vertx);

// We need cookies, sessions and request bodies
router.route().handler(CookieHandler.create().handle);
router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).handle);
router.route().handler(BodyHandler.create().handle);

// Simple auth service which uses a properties file for user/role info
var authProvider = ShiroAuth.create(vertx, 'PROPERTIES', {
});

// Any requests to URI starting '/private/' require login
router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/loginpage.html").handle);

// Serve the static private pages from directory 'private'
router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private").handle);

// Handles the actual login
router.route("/loginhandler").handler(FormLoginHandler.create(authProvider).handle);

// Implement logout
router.route("/logout").handler(function (context) {
  context.setUser(null);
  // Redirect back to the index page
  context.response().putHeader("location", "/").setStatusCode(302).end();
});

// Serve the non private static pages
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.accept).listen(8080);

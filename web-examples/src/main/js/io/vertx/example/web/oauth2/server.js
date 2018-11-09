var Router = require("vertx-web-js/router");
var CookieHandler = require("vertx-web-js/cookie_handler");
var LocalSessionStore = require("vertx-web-js/local_session_store");
var SessionHandler = require("vertx-web-js/session_handler");
var GithubAuth = require("vertx-auth-oauth2-js/github_auth");
var UserSessionHandler = require("vertx-web-js/user_session_handler");
var OAuth2AuthHandler = require("vertx-web-js/o_auth2_auth_handler");
var AccessToken = require("vertx-auth-oauth2-js/access_token");
var HandlebarsTemplateEngine = require("vertx-web-templ-handlebars-js/handlebars_template_engine");
var CLIENT_SECRET = "3155eafd33fc947e0fe9f44127055ce1fe876704";
var CLIENT_ID = "57cdaa1952a3f4ee3df8";
var engine = HandlebarsTemplateEngine.create(vertx);
// To simplify the development of the web components we use a Router to route all HTTP requests
// to organize our code in a reusable way.
var router = Router.router(vertx);
// We need cookies and sessions
router.route().handler(CookieHandler.create().handle);
router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).handle);
// Simple auth service which uses a GitHub to authenticate the user
var authProvider = GithubAuth.create(vertx, CLIENT_ID, CLIENT_SECRET);
// We need a user session handler too to make sure the user is stored in the session between requests
router.route().handler(UserSessionHandler.create(authProvider).handle);
// we now protect the resource under the path "/protected"
router.route("/protected").handler(OAuth2AuthHandler.create(authProvider).setupCallback(router.route("/callback")).addAuthority("user:email").handle);
// Entry point to the application, this will render a custom template.
router.get("/").handler(function (ctx) {
  // we pass the client id to the template
  var data = {
    "client_id" : CLIENT_ID
  };
  // and now delegate to the engine to render it.
  engine.render(data, "views/index.hbs", function (res, res_err) {
    if (res_err == null) {
      ctx.response().putHeader("Content-Type", "text/html").end(res);
    } else {
      ctx.fail(res_err);
    }
  });
});
// The protected resource
router.get("/protected").handler(function (ctx) {
  var user = ctx.user();
  // retrieve the user profile, this is a common feature but not from the official OAuth2 spec
  user.userInfo(function (res, res_err) {
    if (res_err != null) {
      // request didn't succeed because the token was revoked so we
      // invalidate the token stored in the session and render the
      // index page so that the user can start the OAuth flow again
      ctx.session().destroy();
      ctx.fail(res_err);
    } else {
      // the request succeeded, so we use the API to fetch the user's emails
      var userInfo = res;

      // fetch the user emails from the github API

      // the fetch method will retrieve any resource and ensure the right
      // secure headers are passed.
      user.fetch("https://api.github.com/user/emails", function (res2, res2_err) {
        if (res2_err != null) {
          // request didn't succeed because the token was revoked so we
          // invalidate the token stored in the session and render the
          // index page so that the user can start the OAuth flow again
          ctx.session().destroy();
          ctx.fail(res2_err);
        } else {
          userInfo.private_emails = res2.jsonArray();
          // we pass the client info to the template
          var data = {
            "userInfo" : userInfo
          };
          // and now delegate to the engine to render it.
          engine.render(data, "views/advanced.hbs", function (res3, res3_err) {
            if (res3_err == null) {
              ctx.response().putHeader("Content-Type", "text/html").end(res3);
            } else {
              ctx.fail(res3_err);
            }
          });
        }
      });
    }
  });
});

vertx.createHttpServer().requestHandler(router.accept).listen(8080);

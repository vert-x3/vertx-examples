var Router = require("vertx-web-js/router");
var JWTAuth = require("vertx-auth-jwt-js/jwt_auth");
var JWTAuthHandler = require("vertx-web-js/jwt_auth_handler");
var StaticHandler = require("vertx-web-js/static_handler");

var router = Router.router(vertx);

// Create a JWT Auth Provider
var jwt = JWTAuth.create(vertx, {
  "keyStore" : {
    "type" : "jceks",
    "path" : "keystore.jceks",
    "password" : "secret"
  }
});

// protect the API
router.route("/api/*").handler(JWTAuthHandler.create(jwt, "/api/newToken").handle);

// this route is excluded from the auth handler
router.get("/api/newToken").handler(function (ctx) {
  ctx.response().putHeader("Content-Type", "text/plain");
  ctx.response().end(jwt.generateToken({
  }, {
    "expiresInSeconds" : 60
  }));
});

// this is the secret API
router.get("/api/protected").handler(function (ctx) {
  ctx.response().putHeader("Content-Type", "text/plain");
  ctx.response().end("a secret you should keep for yourself...");
});

// Serve the non private static pages
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.handle).listen(8080);

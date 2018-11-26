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

// this route is excluded from the auth handler (it represents your login endpoint)
router.get("/api/newToken").handler(function (ctx) {

  var authorities = [];

  Array.prototype.forEach.call(ctx.request().params().getAll("authority"), function(authority) {
    authorities.push(authority);
  });

  ctx.response().putHeader("Content-Type", "text/plain");
  ctx.response().end(jwt.generateToken({
  }, {
    "expiresInSeconds" : 60,
    "permissions" : authorities
  }));
});

// protect the API (any authority is allowed)
router.route("/api/protected").handler(JWTAuthHandler.create(jwt).handle);

router.get("/api/protected").handler(function (ctx) {
  ctx.response().putHeader("Content-Type", "text/plain");
  ctx.response().end("this secret is not defcon!");
});

// protect the API (defcon1 authority is required)
router.route("/api/protected/defcon1").handler(JWTAuthHandler.create(jwt).addAuthority("defcon1").handle);

router.get("/api/protected/defcon1").handler(function (ctx) {
  ctx.response().putHeader("Content-Type", "text/plain");
  ctx.response().end("this secret is defcon1!");
});

// protect the API (defcon2 authority is required)
router.route("/api/protected/defcon2").handler(JWTAuthHandler.create(jwt).addAuthority("defcon2").handle);

router.get("/api/protected/defcon2").handler(function (ctx) {
  ctx.response().putHeader("Content-Type", "text/plain");
  ctx.response().end("this secret is defcon2!");
});

// protect the API (defcon3 authority is required)
router.route("/api/protected/defcon3").handler(JWTAuthHandler.create(jwt).addAuthority("defcon3").handle);

router.get("/api/protected/defcon3").handler(function (ctx) {
  ctx.response().putHeader("Content-Type", "text/plain");
  ctx.response().end("this secret is defcon3!");
});

// Serve the non private static pages
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.handle).listen(8080);

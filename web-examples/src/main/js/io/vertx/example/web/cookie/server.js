var Router = require("vertx-web-js/router");
var CookieHandler = require("vertx-web-js/cookie_handler");
var Cookie = require("vertx-web-js/cookie");
var StaticHandler = require("vertx-web-js/static_handler");

var router = Router.router(vertx);

// This cookie handler will be called for all routes
router.route().handler(CookieHandler.create().handle);

// on every path increment the counter
router.route().handler(function (ctx) {
  var someCookie = ctx.getCookie("visits");

  var visits = 0;
  if ((someCookie !== null && someCookie !== undefined)) {
    var cookieValue = someCookie.getValue();
    try {
      visits = Java.type("java.lang.Long").parseLong(cookieValue);
    } catch(err) {
      visits = 0;
    }

  }

  // increment the tracking
  visits++;

  // Add a cookie - this will get written back in the response automatically
  ctx.addCookie(Cookie.cookie("visits", "" + visits));

  ctx.next();
});

// Serve the static resources
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.handle).listen(8080);

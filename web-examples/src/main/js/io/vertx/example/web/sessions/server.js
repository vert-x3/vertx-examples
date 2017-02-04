var Router = require("vertx-web-js/router");
var CookieHandler = require("vertx-web-js/cookie_handler");
var LocalSessionStore = require("vertx-web-js/local_session_store");
var SessionHandler = require("vertx-web-js/session_handler");

var router = Router.router(vertx);

router.route().handler(CookieHandler.create().handle);
router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)).handle);

router.route().handler(function (routingContext) {

  var session = routingContext.session();

  var cnt = session.get("hitcount");
  cnt = (cnt === null ||cnt === undefined ? 0 : cnt) + 1;

  session.put("hitcount", cnt);

  routingContext.response().putHeader("content-type", "text/html").end("<html><body><h1>Hitcount: " + cnt + "</h1></body></html>");
});

vertx.createHttpServer().requestHandler(router.accept).listen(8080);

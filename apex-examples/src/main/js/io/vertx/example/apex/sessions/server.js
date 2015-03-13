var Router = require("vertx-apex-js/router");
var CookieHandler = require("vertx-apex-js/cookie_handler");
var SessionHandler = require("vertx-apex-js/session_handler");
var LocalSessionStore = require("vertx-apex-js/local_session_store");

var router = Router.router(vertx);

router.route().handler(CookieHandler.create());
router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

router.route().handler(function (routingContext) {

  var session = routingContext.session();

  var cnt = session.get("hitcount");
  cnt = (cnt === null ? 0 : cnt) + 1;

  session.put("hitcount", cnt);

  routingContext.response().putHeader("content-type", "text/html").end("<html><body><h1>Hitcount: " + cnt + "</h1></body></html>");
});

vertx.createHttpServer().requestHandler(router.accept).listen(8080);

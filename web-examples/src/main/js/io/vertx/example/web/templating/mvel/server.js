var Router = require("vertx-web-js/router");
var MVELTemplateEngine = require("vertx-web-templ-mvel-js/mvel_template_engine");
var TemplateHandler = require("vertx-web-js/template_handler");
var StaticHandler = require("vertx-web-js/static_handler");

var router = Router.router(vertx);

// Serve the dynamic pages
router.route("/dynamic/*").handler(function (ctx) {
  // put the context into the template render context
  ctx.put("context", ctx);
  ctx.next();
}).handler(TemplateHandler.create(MVELTemplateEngine.create(vertx)).handle);

// Serve the static pages
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.accept).listen(8080);

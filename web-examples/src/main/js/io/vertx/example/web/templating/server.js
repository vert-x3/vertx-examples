var Router = require("vertx-web-js/router");
var TemplateHandler = require("vertx-web-js/template_handler");
var MVELTemplateEngine = require("vertx-web-js/mvel_template_engine");
var StaticHandler = require("vertx-web-js/static_handler");

var router = Router.router(vertx);

// Serve the dynamic pages
router.route("/dynamic/*").handler(TemplateHandler.create(MVELTemplateEngine.create()).handle);

// Serve the static pages
router.route().handler(StaticHandler.create().handle);

vertx.createHttpServer().requestHandler(router.accept).listen(8080);

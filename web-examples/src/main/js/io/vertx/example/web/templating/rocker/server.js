var Router = require("vertx-web-js/router");
var RockerTemplateEngine = require("vertx-web-templ-rocker-js/rocker_template_engine");
var TemplateHandler = require("vertx-web-js/template_handler");

var router = Router.router(vertx);

// Populate context with data
router.route().handler(function (ctx) {
  ctx.put("title", "Vert.x Web Example Using Rocker");
  ctx.put("name", "Rocker");
  ctx.next();
});

// Render a custom template.
// Note: you need a compile-time generator for Rocker to work properly
// See the pom.xml for an example
router.route().handler(TemplateHandler.create(RockerTemplateEngine.create()).handle);

vertx.createHttpServer().requestHandler(router.handle).listen(8080);

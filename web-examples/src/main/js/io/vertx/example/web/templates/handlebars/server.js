var HandlebarsTemplateEngine = require("vertx-web-js/handlebars_template_engine");
var Router = require("vertx-web-js/router");

// In order to use a template we first need to create an engine
var engine = HandlebarsTemplateEngine.create();

// To simplify the development of the web components we use a Router to route all HTTP requests
// to organize our code in a reusable way.
var router = Router.router(vertx);

// Entry point to the application, this will render a custom template.
router.get().handler(function (ctx) {
  // we define a hardcoded title for our application
  ctx.put("name", "Vert.x Web");

  // and now delegate to the engine to render it.
  engine.render(ctx, "templates/index.hbs", function (res, res_err) {
    if (res_err == null) {
      ctx.response().putHeader(Java.type("io.vertx.core.http.HttpHeaders").CONTENT_TYPE, "text/html").end(res);
    } else {
      ctx.fail(res_err);
    }
  });
});

// start a HTTP web server on port 8080
vertx.createHttpServer().requestHandler(router.accept).listen(8080);

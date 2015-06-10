var MongoClient = require("vertx-mongo-js/mongo_client");
var JadeTemplateEngine = require("vertx-web-js/jade_template_engine");
var Router = require("vertx-web-js/router");
var BodyHandler = require("vertx-web-js/body_handler");
var StaticHandler = require("vertx-web-js/static_handler");

// Create a mongo client using all defaults (connect to localhost and default port) using the database name "demo".
var mongo = MongoClient.createShared(vertx, {
  "db_name" : "demo"
});

// In order to use a JADE template we first need to create an engine
var jade = JadeTemplateEngine.create();

// To simplify the development of the web components we use a Router to route all HTTP requests
// to organize our code in a reusable way.
var router = Router.router(vertx);

// Enable the body parser to we can get the form data and json documents in out context.
router.route().handler(BodyHandler.create().handle);

// Entry point to the application, this will render a custom JADE template.
router.get("/").handler(function (ctx) {
  // we define a hardcoded title for our application
  ctx.put("title", "Vert.x Web");

  // and now delegate to the engine to render it.
  jade.render(ctx, "templates/index", function (res, res_err) {
    if (res_err == null) {
      ctx.response().putHeader(Java.type("io.vertx.core.http.HttpHeaders").CONTENT_TYPE, "text/html").end(res);
    } else {
      ctx.fail(res_err);
    }
  });
});

// and now we mount the handlers in their appropriate routes

// Read all users from the mongo collection.
router.get("/users").handler(function (ctx) {
  // issue a find command to mongo to fetch all documents from the "users" collection.
  mongo.find("users", {
  }, function (lookup, lookup_err) {
    // error handling
    if (lookup_err != null) {
      ctx.fail(lookup_err);
      return
    }

    // now convert the list to a JsonArray because it will be easier to encode the final object as the response.
    var json = [
    ];
    Array.prototype.forEach.call(lookup, function(o) {
      json.push(o);
    });

    // since we are producing json we should inform the browser of the correct content type.
    ctx.response().putHeader(Java.type("io.vertx.core.http.HttpHeaders").CONTENT_TYPE, "application/json");
    // encode to json string
    ctx.response().end(JSON.stringify(json));
  });
});

// Create a new document on mongo.
router.post("/users").handler(function (ctx) {
  // since jquery is sending data in multipart-form format to avoid preflight calls, we need to convert it to JSON.
  var user = {
    "username" : ctx.request().getFormAttribute("username"),
    "email" : ctx.request().getFormAttribute("email"),
    "fullname" : ctx.request().getFormAttribute("fullname"),
    "location" : ctx.request().getFormAttribute("location"),
    "age" : ctx.request().getFormAttribute("age"),
    "gender" : ctx.request().getFormAttribute("gender")
  };

  // insert into mongo
  mongo.insert("users", user, function (lookup, lookup_err) {
    // error handling
    if (lookup_err != null) {
      ctx.fail(lookup_err);
      return
    }

    // inform that the document was created
    ctx.response().setStatusCode(201);
    ctx.response().end();
  });
});

// Remove a document from mongo.
router.delete("/users/:id").handler(function (ctx) {
  // catch the id to remove from the url /users/:id and transform it to a mongo query.
  mongo.removeOne("users", {
    "_id" : ctx.request().getParam("id")
  }, function (lookup, lookup_err) {
    // error handling
    if (lookup_err != null) {
      ctx.fail(lookup_err);
      return
    }

    // inform the browser that there is nothing to return.
    ctx.response().setStatusCode(204);
    ctx.response().end();
  });
});

// Serve the non private static pages
router.route().handler(StaticHandler.create().handle);

// start a HTTP web server on port 8080
vertx.createHttpServer().requestHandler(router.accept).listen(8080);

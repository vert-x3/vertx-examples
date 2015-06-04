package io.vertx.example.web.mongo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;

/**
 * This is an example application to showcase the usage of MongDB and Vert.x Web.
 *
 * In this application you will see the usage of:
 *
 *  * JADE templates
 *  * Mongo Client
 *  * Vert.x Web
 *
 * The application allows to list, create and delete mongo documents using a simple web interface.
 *
 * @author <a href="mailto:pmlopes@gmail.com>Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {

    // deploy an embedded mongo do we do not need to worry about not having a local mongo server running.
    // in a production environment you would not do this
    vertx.deployVerticle("maven:io.vertx:vertx-mongo-embedded-db:3.0.0-milestone6");

    // Create a mongo client using all defaults (connect to localhost and default port) using the database name "demo".
    final MongoClient mongo = MongoClient.createShared(vertx, config().put("db_name", "demo"));

    // In order to use a JADE template we first need to create an engine
    final JadeTemplateEngine jade = JadeTemplateEngine.create();

    // To simplify the development of the web components we use a Router to route all HTTP requests
    // to organize our code in a reusable way.
    final Router router = Router.router(vertx);

    // Enable the body parser to we can get the form data and json documents in out context.
    router.route().handler(BodyHandler.create());

    // Entry point to the application, this will render a custom JADE template.
    router.get("/").handler(ctx -> {
      // we define a hardcoded title for our application
      ctx.put("title", "Vert.x Web");

      // and now delegate to the engine to render it.
      jade.render(ctx, "templates/index", res -> {
        if (res.succeeded()) {
          ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
        } else {
          ctx.fail(res.cause());
        }
      });
    });

    // In order to handle all mongo related requests we organize the handlers in a seperated class
    final Users users = new Users()
      .setMongo(mongo);

    // and now we mount the handlers in their appropriate routes

    // Read all users from the mongo collection.
    router.get("/users").handler(ctx -> {
      // issue a find command to mongo to fetch all documents from the "users" collection.
      mongo.find("users", new JsonObject(), lookup -> {
        // error handling
        if (lookup.failed()) {
          ctx.fail(lookup.cause());
          return;
        }

        // now convert the list to a JsonArray because it will be easier to encode the final object as the response.
        final JsonArray json = new JsonArray();

        lookup.result().forEach(json::add);

        // since we are producing json we should inform the browser of the correct content type.
        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        // encode to json string
        ctx.response().end(json.encode());
      });
    });

    // Create a new document on mongo.
    router.post("/users").handler(ctx -> {
      // since jquery is sending data in multipart-form format to avoid preflight calls, we need to convert it to JSON.
      JsonObject user = new JsonObject()
              .put("username", ctx.request().getFormAttribute("username"))
              .put("email", ctx.request().getFormAttribute("email"))
              .put("fullname", ctx.request().getFormAttribute("fullname"))
              .put("location", ctx.request().getFormAttribute("location"))
              .put("age", ctx.request().getFormAttribute("age"))
              .put("gender", ctx.request().getFormAttribute("gender"));

      // insert into mongo
      mongo.insert("users", user, lookup -> {
        // error handling
        if (lookup.failed()) {
          ctx.fail(lookup.cause());
          return;
        }

        // inform that the document was created
        ctx.response().setStatusCode(201);
        ctx.response().end();
      });
    });

    // Remove a document from mongo.
    router.delete("/users/:id").handler(ctx -> {
      // catch the id to remove from the url /users/:id and transform it to a mongo query.
      mongo.removeOne("users", new JsonObject().put("_id", ctx.request().getParam("id")), lookup -> {
        // error handling
        if (lookup.failed()) {
          ctx.fail(lookup.cause());
          return;
        }

        // inform the browser that there is nothing to return.
        ctx.response().setStatusCode(204);
        ctx.response().end();
      });
    });

    // Serve the non private static pages
    router.route().handler(StaticHandler.create());

    // start a HTTP web server on port 8080
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
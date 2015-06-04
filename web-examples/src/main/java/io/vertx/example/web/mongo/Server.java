package io.vertx.example.web.mongo;

import io.vertx.core.AbstractVerticle;
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
    final MongoClient mongo = MongoClient.createNonShared(vertx, config().put("db_name", "demo"));

    // To simplify the development of the web components we use a Router to route all HTTP requests
    // to organize our code in a reusable way.
    final Router router = Router.router(vertx);

    // we expect the post to be in multipart form data format (to avoid having to deal with preflight requests
    router.route().handler(ctx -> {
      ctx.request().setExpectMultipart(true);
      ctx.next();
    });

    // Enable the body parser to we can get the form data and json documents in out context.
    router.route().handler(BodyHandler.create());

    // Entry point to the application, this will render a custom JADE template.
    router.get("/").handler(Routes::index);

    // In order to handle all mongo related requests we organize the handlers in a seperated class
    final Users users = new Users()
      .setMongo(mongo);

    // and now we mount the handlers in their appropriate routes
    router.get("/users").handler(users::read);
    router.post("/users").handler(users::create);
    router.delete("/users/:id").handler(users::delete);

    // Serve the non private static pages
    router.route().handler(StaticHandler.create());

    // start a HTTP web server on port 8080
    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }
}
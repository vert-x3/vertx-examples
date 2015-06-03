package io.vertx.example.web.mongo;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

/**
 * This class handles all the routes that use mongo to read and load data.
 *
 * @author <a href="mailto:pmlopes@gmail.com>Paulo Lopes</a>
 */
public class Users {

  private MongoClient mongo;

  public Users setMongo(MongoClient mongo) {
    this.mongo = mongo;
    return this;
  }

  /**
   * Read all users from the mongo collection.
   *
   * @param ctx the routing context.
   */
  public void read(RoutingContext ctx) {
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
  }

  /**
   * Create a new document on mongo.
   *
   * @param ctx the routing context.
   */
  public void create(RoutingContext ctx) {
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
  }

  /**
   * Remove a document from mongo.
   *
   * @param ctx the routing context.
   */
  public void delete(RoutingContext ctx) {
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
  }
}

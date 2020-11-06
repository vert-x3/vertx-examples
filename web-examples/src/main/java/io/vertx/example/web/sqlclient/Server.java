/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.example.web.sqlclient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.SqlResult;
import io.vertx.sqlclient.templates.SqlTemplate;
import io.vertx.sqlclient.templates.TupleMapper;

import java.util.Collections;
import java.util.Map;

/**
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  private JDBCPool client;
  private SqlTemplate<Map<String, Object>, RowSet<JsonObject>> getProductTmpl;
  private SqlTemplate<JsonObject, SqlResult<Void>> addProductTmpl;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    // Create a JDBC client with a test database
    client = JDBCPool.pool(vertx, new JsonObject()
      .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver"));
    getProductTmpl = SqlTemplate
      .forQuery(client, "SELECT id, name, price, weight FROM products where id = #{id}")
      .mapTo(Row::toJson);
    addProductTmpl = SqlTemplate
      .forUpdate(client, "INSERT INTO products (name, price, weight) VALUES (#{name}, #{price}, #{weight})")
      .mapFrom(TupleMapper.jsonObject());

    Handler<RoutingContext> getProductRoute = Server.this::handleGetProduct;
    Handler<RoutingContext> addProductRoute = Server.this::handleAddProduct;
    Handler<RoutingContext> listProductsRoute = Server.this::handleListProducts;

    setUpInitialData().flatMap(v -> {
      Router router = Router.router(vertx);

      router.route().handler(BodyHandler.create());

      // in order to minimize the nesting of call backs we can put the JDBC connection on the context for all routes
      // that match /products
      // this should really be encapsulated in a reusable JDBC handler that uses can just add to their app
      router.route("/products*").handler(routingContext -> client.getConnection(res -> {
        if (res.failed()) {
          routingContext.fail(res.cause());
        } else {
          SqlConnection conn = res.result();

          // save the connection on the context
          routingContext.put("conn", conn);

          // we need to return the connection back to the jdbc pool. In order to do that we need to close it, to keep
          // the remaining code readable one can add a headers end handler to close the connection.
          routingContext.addHeadersEndHandler(done -> conn.close());

          routingContext.next();
        }
      }));

      router.get("/products/:productID").handler(getProductRoute);
      router.post("/products").handler(addProductRoute);
      router.get("/products").handler(listProductsRoute);

      return vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(8080);
    })
      .<Void>mapEmpty()
      .onComplete(startPromise);
  }

  private void handleGetProduct(RoutingContext routingContext) {
    String productID = routingContext.request().getParam("productID");
    HttpServerResponse response = routingContext.response();
    if (productID == null) {
      sendError(400, response);
    } else {
      getProductTmpl
        .execute(Collections.singletonMap("id", productID))
        .onSuccess(result -> {
          if (result.size() == 0) {
            sendError(404, response);
          } else {
            response
              .putHeader("content-type", "application/json")
              .end(result.iterator().next().encode());
          }
      }).onFailure(err -> {
        sendError(500, response);
      });
    }
  }

  private void handleAddProduct(RoutingContext routingContext) {

    HttpServerResponse response = routingContext.response();

    JsonObject product = routingContext.getBodyAsJson();

    addProductTmpl.execute(product)
      .onSuccess(res -> response.end())
      .onFailure(err -> sendError(500, response));
  }

  private void handleListProducts(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    client.query("SELECT id, name, price, weight FROM products").execute(query -> {
      if (query.failed()) {
        sendError(500, response);
      } else {
        JsonArray arr = new JsonArray();
        query.result().forEach(row -> {
          arr.add(row.toJson());
        });
        routingContext.response().putHeader("content-type", "application/json").end(arr.encode());
      }
    });
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

  private Future<Void> setUpInitialData() {
    return client.getConnection()
      .flatMap(conn -> conn
        .query("CREATE TABLE IF NOT EXISTS products(id INT IDENTITY, name VARCHAR(255), price FLOAT, weight INT)")
        .execute()
        .flatMap(res -> conn
          .query("INSERT INTO products (name, price, weight) VALUES ('Egg Whisk', 3.99, 150), ('Tea Cosy', 5.99, 100), ('Spatula', 1.00, 80)")
          .execute())
      ).mapEmpty();
  }
}

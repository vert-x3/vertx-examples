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

package io.vertx.example.web.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  private JDBCClient client;

  @Override
  public void start() {

    Server that = this;

    // Create a JDBC client with a test database
    client = JDBCClient.createShared(vertx, new JsonObject()
      .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver"));

    setUpInitialData(ready -> {
      Router router = Router.router(vertx);

      router.route().handler(BodyHandler.create());

      // in order to minimize the nesting of call backs we can put the JDBC connection on the context for all routes
      // that match /products
      // this should really be encapsulated in a reusable JDBC handler that uses can just add to their app
      router.route("/products*").handler(routingContext -> client.getConnection(res -> {
        if (res.failed()) {
          routingContext.fail(res.cause());
        } else {
          SQLConnection conn = res.result();

          // save the connection on the context
          routingContext.put("conn", conn);

          // we need to return the connection back to the jdbc pool. In order to do that we need to close it, to keep
          // the remaining code readable one can add a headers end handler to close the connection.
          routingContext.addHeadersEndHandler(done -> conn.close(v -> { }));

          routingContext.next();
        }
      })).failureHandler(routingContext -> {
        SQLConnection conn = routingContext.get("conn");
        if (conn != null) {
          conn.close(v -> {
          });
        }
      });

      router.get("/products/:productID").handler(that::handleGetProduct);
      router.post("/products").handler(that::handleAddProduct);
      router.get("/products").handler(that::handleListProducts);

      vertx.createHttpServer().requestHandler(router).listen(8080);
    });
  }

  private void handleGetProduct(RoutingContext routingContext) {
    String productID = routingContext.request().getParam("productID");
    HttpServerResponse response = routingContext.response();
    if (productID == null) {
      sendError(400, response);
    } else {
      SQLConnection conn = routingContext.get("conn");

      conn.queryWithParams("SELECT id, name, price, weight FROM products where id = ?", new JsonArray().add(Integer.parseInt(productID)), query -> {
        if (query.failed()) {
          sendError(500, response);
        } else {
          if (query.result().getNumRows() == 0) {
            sendError(404, response);
          } else {
            response.putHeader("content-type", "application/json").end(query.result().getRows().get(0).encode());
          }
        }
      });
    }
  }

  private void handleAddProduct(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();

    SQLConnection conn = routingContext.get("conn");
    JsonObject product = routingContext.getBodyAsJson();

    conn.updateWithParams("INSERT INTO products (name, price, weight) VALUES (?, ?, ?)",
      new JsonArray().add(product.getString("name")).add(product.getFloat("price")).add(product.getInteger("weight")), query -> {
        if (query.failed()) {
          sendError(500, response);
        } else {
          response.end();
        }
      });
  }

  private void handleListProducts(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    SQLConnection conn = routingContext.get("conn");

    conn.query("SELECT id, name, price, weight FROM products", query -> {
      if (query.failed()) {
        sendError(500, response);
      } else {
        JsonArray arr = new JsonArray();
        query.result().getRows().forEach(arr::add);
        routingContext.response().putHeader("content-type", "application/json").end(arr.encode());
      }
    });
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

  private void setUpInitialData(Handler<Void> done) {
    client.getConnection(res -> {
      if (res.failed()) {
        throw new RuntimeException(res.cause());
      }

      final SQLConnection conn = res.result();

      conn.execute("CREATE TABLE IF NOT EXISTS products(id INT IDENTITY, name VARCHAR(255), price FLOAT, weight INT)", ddl -> {
        if (ddl.failed()) {
          throw new RuntimeException(ddl.cause());
        }

        conn.execute("INSERT INTO products (name, price, weight) VALUES ('Egg Whisk', 3.99, 150), ('Tea Cosy', 5.99, 100), ('Spatula', 1.00, 80)", fixtures -> {
          if (fixtures.failed()) {
            throw new RuntimeException(fixtures.cause());
          }

          done.handle(null);
        });
      });
    });
  }
}

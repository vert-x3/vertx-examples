import io.vertx.ceylon.web { ... }
import io.vertx.ceylon.core { Verticle }
import io.vertx.ceylon.jdbc { ... }
import io.vertx.ceylon.sql { SQLConnection,
  ResultSet,
  UpdateResult }
import ceylon.json { JsonObject=Object, JsonArray=Array }
import io.vertx.ceylon.web.handler {
  bodyHandler
}
import io.vertx.ceylon.core.http {
  HttpServerResponse
}

shared class Server() extends Verticle() {
  
  shared actual void start() {
    
    value client = jdbcClient.createShared(vertx, JsonObject {
      "url"->"jdbc:hsqldb:mem:test?shutdown=true",
      "driver_class"->"org.hsqldb.jdbcDriver"
    });
    
    setUpInitialData(client, () {
      
      value router_ = router.router(vertx);
      
      router_.route().handler(bodyHandler.create().handle);
      
      // in order to minimize the nesting of call backs we can put the JDBC connection on the context for all routes
      // that match /products
      // this should really be encapsulated in a reusable JDBC handler that uses can just add to their app
      router_.route("/products/*").handler {
        void requestHandler(RoutingContext routingContext) {
          
          client.getConnection((SQLConnection|Throwable conn) {
            if (is Throwable conn) {
              routingContext.fail(conn);
              return;
            }
            
            // save the connection on the context
            routingContext.put("conn", conn);
            
            // we need to return the connection back to the jdbc pool. In order to do that we need to close it, to keep
            // the remaining code readable one can add a headers end handler to close the connection.
            routingContext.addHeadersEndHandler(() => conn.close());
            
            routingContext.next();            
          });
        }
      }.failureHandler {
        void failureHandler(RoutingContext routingContext) {
          if (exists conn = routingContext.get<SQLConnection>("conn")) {
            conn.close();
          }
        }
      };
      
      router_.get("/products/:productID").handler(handleGetProduct);
      router_.post("/products").handler(handleAddProduct);
      router_.get("/products").handler(handleListProducts);
      
      vertx.createHttpServer().requestHandler(router_.accept).listen(8080);
    });
  }
  
  void handleGetProduct(RoutingContext routingContext) {
    value response = routingContext.response();
    if (exists productID = routingContext.request().getParam("productID")) {
      assert(exists conn = routingContext.get<SQLConnection>("conn"));
      conn.queryWithParams("SELECT id, name, price, weight FROM products where id = ?", JsonArray {
        parseInteger(productID)
      }, (ResultSet|Throwable result) {
        if (is Throwable result) {
          sendError(500, response);
          return;
        }
        if (exists row = result.rows?.first) {
          response.putHeader("content-type", "application/json").end(row.string);
        } else {
          sendError(404, response);
        }
      });
    } else {
      sendError(400, response);
    }
  }
  
  void handleAddProduct(RoutingContext routingContext) {
    value response = routingContext.response();
    assert(exists conn = routingContext.get<SQLConnection>("conn"));
    value product = routingContext.getBodyAsJson();
    if (exists product) {
      conn.updateWithParams("INSERT INTO products (name, price, weight) VALUES (?, ?, ?)", JsonArray {
        product.getString("name"), product.getFloat("price"), product.getInteger("weight")
      }, (UpdateResult|Throwable result) {
        if (is Throwable result) {
          sendError(500, response);
          return;
        }
        response.end();
      });
    } else {
      sendError(500, response);
    }    
  }
  
  void handleListProducts(RoutingContext routingContext) {
    value response = routingContext.response();
    assert(exists conn = routingContext.get<SQLConnection>("conn"));
    conn.query("SELECT id, name, price, weight FROM products", (ResultSet|Throwable result) {
      if (is Throwable result) {
        sendError(500, response);
        return;
      }
      value arr = JsonArray();
      result.rows?.each(arr.add);
      response.putHeader("content-type", "application/json").end(arr.string);
    });
    
  }
  
  void sendError(Integer statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }
  
  void setUpInitialData(JDBCClient client, void done()) {
    print("calling get connection");
    client.getConnection((SQLConnection|Throwable conn) {
      if (is Throwable conn) {
        throw conn;
      }
      conn.execute("CREATE TABLE IF NOT EXISTS products(id INT IDENTITY, name VARCHAR(255), price FLOAT, weight INT)", (Throwable? err) {
        if (exists err) {
          throw err;
        }
        conn.execute("INSERT INTO products (name, price, weight) VALUES ('Egg Whisk', 3.99, 150), ('Tea Cosy', 5.99, 100), ('Spatula', 1.00, 80)", (Throwable? err) {
          if (exists err) {
            throw err;
          }
          done();
        });
      });
    });    
  }
}
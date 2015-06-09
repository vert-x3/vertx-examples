var JDBCClient = require("vertx-jdbc-js/jdbc_client");
var Router = require("vertx-web-js/router");
var BodyHandler = require("vertx-web-js/body_handler");
var client;
var setUpInitialData = function(done) {
  client.getConnection(function (res, res_err) {
    if (res_err != null) {
      throw res_err;
    }

    var conn = res;

    conn.execute("CREATE TABLE IF NOT EXISTS products(id INT IDENTITY, name VARCHAR(255), price FLOAT, weight INT)", function (ddl, ddl_err) {
      if (ddl_err != null) {
        throw ddl_err;
      }

      conn.execute("INSERT INTO products (name, price, weight) VALUES ('Egg Whisk', 3.99, 150), ('Tea Cosy', 5.99, 100), ('Spatula', 1.00, 80)", function (fixtures, fixtures_err) {
        if (fixtures_err != null) {
          throw fixtures_err;
        }

        done.handle(null);
      });
    });
  });
};

var that = this;

// Create a JDBC client with a test database
client = JDBCClient.createShared(vertx, {
  "url" : "jdbc:hsqldb:mem:test?shutdown=true",
  "driver_class" : "org.hsqldb.jdbcDriver"
});

setUpInitialData(function (ready) {
  var router = Router.router(vertx);

  router.route().handler(BodyHandler.create().handle);

  // in order to minimize the nesting of call backs we can put the JDBC connection on the context for all routes
  // that match /products
  router.route("/products*").handler(function (routingContext) {
    client.getConnection(function (res, res_err) {
      if (res_err != null) {
        routingContext.fail(500);
      } else {
        var conn = res;

        // save the connection on the context
        routingContext.put("conn", conn);

        // we need to return the connection back to the jdbc pool. In order to do that we need to close it, to keep
        // the remaining code readable one can add a headers end handler to close the connection. The reason to
        // choose the headers end is that if the close of the connection or say for example end of transaction
        // results in an error, it is still possible to return back to the client an error code and message.
        routingContext.addHeadersEndHandler(function (done) {
          conn.close(function (close, close_err) {
            if (close_err != null) {
              done.fail(close_err);
            } else {
              done.complete();
            }
          });
        });

        routingContext.next();
      }
    });
  });

  router.get("/products/:productID").handler(that.handleGetProduct);
  router.post("/products").handler(that.handleAddProduct);
  router.get("/products").handler(that.handleListProducts);

  vertx.createHttpServer().requestHandler(router.accept).listen(8080);
});

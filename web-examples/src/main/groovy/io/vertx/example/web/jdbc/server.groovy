import groovy.transform.Field
import io.vertx.groovy.ext.jdbc.JDBCClient
import io.vertx.groovy.ext.web.Router
import io.vertx.groovy.ext.web.handler.BodyHandler
@Field def client
def setUpInitialData(done) {
  client.getConnection({ res ->
    if (res.failed()) {
      throw new java.lang.RuntimeException(res.cause())
    }

    def conn = res.result()

    conn.execute("CREATE TABLE IF NOT EXISTS products(id INT IDENTITY, name VARCHAR(255), price FLOAT, weight INT)", { ddl ->
      if (ddl.failed()) {
        throw new java.lang.RuntimeException(ddl.cause())
      }

      conn.execute("INSERT INTO products (name, price, weight) VALUES ('Egg Whisk', 3.99, 150), ('Tea Cosy', 5.99, 100), ('Spatula', 1.00, 80)", { fixtures ->
        if (fixtures.failed()) {
          throw new java.lang.RuntimeException(fixtures.cause())
        }

        done.handle(null)
      })
    })
  })
}

// Create a JDBC client with a test database
client = JDBCClient.createShared(vertx, [
  url:"jdbc:hsqldb:mem:test?shutdown=true",
  driver_class:"org.hsqldb.jdbcDriver"
])

this.setUpInitialData({ ready ->
  def router = Router.router(vertx)

  router.route().handler(BodyHandler.create())

  // in order to minimize the nesting of call backs we can put the JDBC connection on the context for all routes
  // that match /products
  router.route("/products*").handler({ routingContext ->
    client.getConnection({ res ->
      if (res.failed()) {
        routingContext.fail(500)
      } else {
        def conn = res.result()

        // save the connection on the context
        routingContext.put("conn", conn)

        // we need to return the connection back to the jdbc pool. In order to do that we need to close it, to keep
        // the remaining code readable one can add a headers end handler to close the connection. The reason to
        // choose the headers end is that if the close of the connection or say for example end of transaction
        // results in an error, it is still possible to return back to the client an error code and message.
        routingContext.addHeadersEndHandler({ end ->
          conn.close({ close ->
            if (close.failed()) {
              end.fail(close.cause())
            } else {
              end.complete()
            }
          })
        })

        routingContext.next()
      }
    })
  })

  router.get("/products/:productID").handler(io.vertx.example.web.jdbc.Server.this.&handleGetProduct)
  router.post("/products").handler(io.vertx.example.web.jdbc.Server.this.&handleAddProduct)
  router.get("/products").handler(io.vertx.example.web.jdbc.Server.this.&handleListProducts)

  vertx.createHttpServer().requestHandler(router.&accept).listen(8080)
})

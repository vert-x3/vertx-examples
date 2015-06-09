require 'vertx-jdbc/jdbc_client'
require 'vertx-web/router'
require 'vertx-web/body_handler'
@client
def set_up_initial_data(done)
  @client.get_connection() { |res,res_err|
    if (res_err != nil)
      raise res_err
    end

    conn = res

    conn.execute("CREATE TABLE IF NOT EXISTS products(id INT IDENTITY, name VARCHAR(255), price FLOAT, weight INT)") { |ddl,ddl_err|
      if (ddl_err != nil)
        raise ddl_err
      end

      conn.execute("INSERT INTO products (name, price, weight) VALUES ('Egg Whisk', 3.99, 150), ('Tea Cosy', 5.99, 100), ('Spatula', 1.00, 80)") { |fixtures,fixtures_err|
        if (fixtures_err != nil)
          raise fixtures_err
        end

        done.handle(nil)
      }
    }
  }
end

# Create a JDBC client with a test database
@client = VertxJdbc::JDBCClient.create_shared($vertx, {
  'url' => "jdbc:hsqldb:mem:test?shutdown=true",
  'driver_class' => "org.hsqldb.jdbcDriver"
})

set_up_initial_data() { |ready|
  router = VertxWeb::Router.router($vertx)

  router.route().handler(&VertxWeb::BodyHandler.create().method(:handle))

  # in order to minimize the nesting of call backs we can put the JDBC connection on the context for all routes
  # that match /products
  router.route("/products*").handler() { |routingContext|
    @client.get_connection() { |res,res_err|
      if (res_err != nil)
        routingContext.fail(500)
      else
        conn = res

        # save the connection on the context
        routingContext.put("conn", conn)

        # we need to return the connection back to the jdbc pool. In order to do that we need to close it, to keep
        # the remaining code readable one can add a headers end handler to close the connection. The reason to
        # choose the headers end is that if the close of the connection or say for example end of transaction
        # results in an error, it is still possible to return back to the client an error code and message.
        routingContext.add_headers_end_handler() { |end|
          conn.close() { |close,close_err|
            if (close_err != nil)
              end.fail(close_err)
            else
              end.complete()
            end
          }
        }

        routingContext.next()
      end
    }
  }

  router.get("/products/:productID").handler(&Java::IoVertxExampleWebJdbc::Server::this.method(:handle_get_product))
  router.post("/products").handler(&Java::IoVertxExampleWebJdbc::Server::this.method(:handle_add_product))
  router.get("/products").handler(&Java::IoVertxExampleWebJdbc::Server::this.method(:handle_list_products))

  $vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)
}

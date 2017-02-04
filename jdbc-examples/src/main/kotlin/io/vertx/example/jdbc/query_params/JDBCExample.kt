package io.vertx.example.jdbc.query_params

import io.vertx.ext.jdbc.JDBCClient
import io.vertx.kotlin.common.json.*

class JDBCExample : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var client = JDBCClient.createShared(vertx, json {
      obj(
        "url" to "jdbc:hsqldb:mem:test?shutdown=true",
        "driver_class" to "org.hsqldb.jdbcDriver",
        "max_pool_size" to 30
      )
    })

    client.getConnection({ conn ->
      if (conn.failed()) {
        System.err.println(conn.cause().getMessage())
        return
      }
      var connection = conn.result()

      // create a test table
      connection.execute("create table test(id int primary key, name varchar(255))", { create ->
        if (create.failed()) {
          System.err.println("Cannot create the table")
          create.cause().printStackTrace()
          return
        }

        // insert some test data
        connection.execute("insert into test values (1, 'Hello'), (2, 'World')", { insert ->

          // query some data with arguments
          connection.queryWithParams("select * from test where id = ?", json {
            array(2)
          }, { rs ->
            if (rs.failed()) {
              System.err.println("Cannot retrieve the data from the database")
              rs.cause().printStackTrace()
              return
            }

            for (line in rs.result().results) {
              println(line.toString())
            }

            // and close the connection
            connection.close({ done ->
              if (done.failed()) {
                throw java.lang.RuntimeException(done.cause())
              }
            })
          })
        })
      })
    })
  }
}

package io.vertx.example.jdbc.streaming

import io.vertx.ext.jdbc.JDBCClient
import io.vertx.kotlin.core.json.*

class JDBCExample : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var client = JDBCClient.createShared(vertx, json {
      obj(
        "url" to "jdbc:hsqldb:mem:test?shutdown=true",
        "driver_class" to "org.hsqldb.jdbcDriver",
        "max_pool_size" to 30,
        "user" to "SA",
        "password" to ""
      )
    })

    client.getConnection({ conn ->
      if (conn.failed()) {
        System.err.println(conn.cause().getMessage())
        return
      }

      var connection = conn.result()
      connection.execute("create table test(id int primary key, name varchar(255))", { res ->
        if (res.failed()) {
          throw java.lang.RuntimeException(res.cause())
        }
        // insert some test data
        connection.execute("insert into test values (1, 'Hello'), (2, 'Goodbye'), (3, 'Cya Later')", { insert ->
          // query some data
          connection.queryStream("select * from test", { stream ->
            if (stream.succeeded()) {
              var sqlRowStream = stream.result()

              sqlRowStream.handler({ row ->
                // do something with the row...
                println(row.toString())
              }).endHandler({ v ->
                // no more data available, close the connection
                connection.close({ done ->
                  if (done.failed()) {
                    throw java.lang.RuntimeException(done.cause())
                  }
                })
              })
            }
          })
        })
      })
    })
  }
}

package io.vertx.example.sqlclient.transaction

import io.vertx.kotlin.pgclient.*
import io.vertx.kotlin.sqlclient.*
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions

class SqlClientExample : io.vertx.core.AbstractVerticle()  {
  override fun start() {

    var pool = PgPool.pool(vertx, PgConnectOptions(
      port = 5432,
      host = "the-host",
      database = "the-db",
      user = "user",
      password = "secret"), PoolOptions(
      maxSize = 4))

    // Uncomment for MySQL
    //    Pool pool = MySQLPool.pool(vertx, new MySQLConnectOptions()
    //      .setPort(5432)
    //      .setHost("the-host")
    //      .setDatabase("the-db")
    //      .setUser("user")
    //      .setPassword("secret"), new PoolOptions().setMaxSize(4));

    pool.begin({ res1 ->
      if (res1.failed()) {
        System.err.println(res1.cause().getMessage())
        return
      }
      var tx = res1.result()

      // create a test table
      tx.query("create table test(id int primary key, name varchar(255))", { res2 ->
        if (res2.failed()) {
          tx.close()
          System.err.println("Cannot create the table")
          res2.cause().printStackTrace()
          return
        }

        // insert some test data
        tx.query("insert into test values (1, 'Hello'), (2, 'World')", { res3 ->

          // query some data with arguments
          tx.query("select * from test", { rs ->
            if (rs.failed()) {
              System.err.println("Cannot retrieve the data from the database")
              rs.cause().printStackTrace()
              return
            }

            for (line in rs.result()) {
              println("${line}")
            }

            // and close the connection
            tx.commit()
          })
        })
      })
    })
  }
}

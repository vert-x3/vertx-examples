package io.vertx.example.sqlclient.query_params

import io.vertx.kotlin.pgclient.*
import io.vertx.kotlin.sqlclient.*
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.Tuple

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

    pool.getConnection({ res1 ->
      if (res1.failed()) {
        System.err.println(res1.cause().getMessage())
        return
      }
      var connection = res1.result()

      // create a test table
      connection.query("create table test(id int primary key, name varchar(255))").execute({ res2 ->
        if (res2.failed()) {
          connection.close()
          System.err.println("Cannot create the table")
          res2.cause().printStackTrace()
          return
        }

        // insert some test data
        connection.query("insert into test values (1, 'Hello'), (2, 'World')").execute({ res3 ->

          // query some data with arguments
          connection.preparedQuery("select * from test where id = ?").execute(Tuple.of(2), { rs ->
            if (rs.failed()) {
              System.err.println("Cannot retrieve the data from the database")
              rs.cause().printStackTrace()
              return
            }

            for (line in rs.result()) {
              println("${line}")
            }

            // and close the connection
            connection.close()
          })
        })
      })
    })
  }
}

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

    pool.getConnection().compose<Any>({ connection ->
      // create a test table
      return connection.query("create table test(id int primary key, name varchar(255))").execute().compose<Any>({ v ->
        // insert some test data
        return connection.query("insert into test values (1, 'Hello'), (2, 'World')").execute()
      }).compose<Any>({ v ->
        // query some data with arguments
        return connection.preparedQuery("select * from test where id = ?").execute(Tuple.of(2))
      }).onSuccess({ rows ->
        for (row in rows) {
          println("row = ${row.toString()}")
        }
      }).onComplete({ v ->
        // and close the connection
        connection.close()
      })
    }).onComplete({ ar ->
      if (ar.succeeded()) {
        println("done")
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
}

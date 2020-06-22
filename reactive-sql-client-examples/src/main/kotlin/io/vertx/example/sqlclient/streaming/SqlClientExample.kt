package io.vertx.example.sqlclient.streaming

import io.vertx.core.Promise
import io.vertx.kotlin.pgclient.*
import io.vertx.kotlin.sqlclient.*
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.Tuple

class SqlClientExample : io.vertx.core.AbstractVerticle() {
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
      var promise = Promise.promise<Any>()
      // create a test table
      connection.query("create table test(id int primary key, name varchar(255))").execute().compose<Any>({ v ->
        // insert some test data
        return connection.query("insert into test values (1, 'Hello'), (2, 'World')").execute()
      }).compose<Any>({ v ->
        // prepare the query
        return connection.prepare("select * from test")
      }).map({ preparedStatement ->
        // create a stream
        return preparedStatement.createStream(50, Tuple.tuple())
      }).onComplete({ ar ->
        if (ar.succeeded()) {
          var stream = ar.result()
          stream.exceptionHandler({ promise.fail(it) }).endHandler({ promise.complete(it) }).handler({ row ->
            println("row = ${row.toString()}")
          })
        } else {
          promise.fail(ar.cause())
        }
      })
      return promise.future().onComplete({ v ->
        // close the connection
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

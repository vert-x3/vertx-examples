import io.vertx.core.Promise
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple

def pool = PgPool.pool(vertx, [
  port    : 5432,
  host    : "the-host",
  database: "the-db",
  user    : "user",
  password: "secret"
], [
  maxSize: 4
])

// Uncomment for MySQL
//    Pool pool = MySQLPool.pool(vertx, new MySQLConnectOptions()
//      .setPort(5432)
//      .setHost("the-host")
//      .setDatabase("the-db")
//      .setUser("user")
//      .setPassword("secret"), new PoolOptions().setMaxSize(4));

pool.getConnection().compose({ connection ->
  def promise = Promise.promise()
  // create a test table
  connection.query("create table test(id int primary key, name varchar(255))").execute().compose({ v ->
    // insert some test data
    return connection.query("insert into test values (1, 'Hello'), (2, 'World')").execute()
  }).compose({ v ->
    // prepare the query
    return connection.prepare("select * from test")
  }).map({ preparedStatement ->
    // create a stream
    return preparedStatement.createStream(50, Tuple.tuple())
  }).onComplete({ ar ->
    if (ar.succeeded()) {
      def stream = ar.result()
      stream.exceptionHandler(promise.&fail).endHandler(promise.&complete).handler({ row ->
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

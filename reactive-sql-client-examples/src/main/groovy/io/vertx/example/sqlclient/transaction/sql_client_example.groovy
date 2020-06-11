import io.vertx.pgclient.PgPool

def pool = PgPool.pool(vertx, [
  port:5432,
  host:"the-host",
  database:"the-db",
  user:"user",
  password:"secret"
], [
  maxSize:4
])

// Uncomment for MySQL
//    Pool pool = MySQLPool.pool(vertx, new MySQLConnectOptions()
//      .setPort(5432)
//      .setHost("the-host")
//      .setDatabase("the-db")
//      .setUser("user")
//      .setPassword("secret"), new PoolOptions().setMaxSize(4));

pool.withTransaction({ sqlClient ->
  // create a test table
  return sqlClient.query("create table test(id int primary key, name varchar(255))").execute().compose({ v ->
    // insert some test data
    return sqlClient.query("insert into test values (1, 'Hello'), (2, 'World')").execute()
  }).compose({ v ->
    // query some data
    return sqlClient.query("select * from test").execute()
  }).onSuccess({ rows ->
    rows.each { row ->
      println("row = ${row.toString()}")
    }
  })
}).onComplete({ ar ->
  if (ar.succeeded()) {
    println("done")
  } else {
    ar.cause().printStackTrace()
  }
})

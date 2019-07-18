import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple

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

pool.getConnection({ res1 ->
  if (res1.failed()) {
    System.err.println(res1.cause().getMessage())
    return
  }
  def connection = res1.result()

  // create a test table
  connection.query("create table test(id int primary key, name varchar(255))", { res2 ->
    if (res2.failed()) {
      connection.close()
      System.err.println("Cannot create the table")
      res2.cause().printStackTrace()
      return
    }

    // insert some test data
    connection.query("insert into test values (1, 'Hello'), (2, 'World')", { res3 ->

      // query some data with arguments
      connection.preparedQuery("select * from test where id = ?", Tuple.of(2), { rs ->
        if (rs.failed()) {
          System.err.println("Cannot retrieve the data from the database")
          rs.cause().printStackTrace()
          return
        }

        rs.result().each { line ->
          println("${line}")
        }

        // and close the connection
        connection.close()
      })
    })
  })
})

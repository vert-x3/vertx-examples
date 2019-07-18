import io.vertx.pgclient.PgPool

def connectOptions = [
  port:5432,
  host:"the-host",
  database:"the-db",
  user:"user",
  password:"secret"
]

def pool = PgPool.pool(vertx, connectOptions, [
  maxSize:4
])

pool.begin({ res1 ->
  if (res1.failed()) {
    System.err.println(res1.cause().getMessage())
    return
  }
  def tx = res1.result()

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

        rs.result().each { line ->
          println("${line}")
        }

        // and close the connection
        tx.commit()
      })
    })
  })
})

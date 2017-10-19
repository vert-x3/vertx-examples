import io.vertx.ext.jdbc.JDBCClient

def client = JDBCClient.createShared(vertx, [
  url:"jdbc:hsqldb:mem:test?shutdown=true",
  driver_class:"org.hsqldb.jdbcDriver",
  max_pool_size:30,
  user:"SA",
  password:""
])

client.getConnection({ conn ->
  if (conn.failed()) {
    System.err.println(conn.cause().getMessage())
    return
  }
  def connection = conn.result()

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
      connection.queryWithParams("select * from test where id = ?", [
        2
      ], { rs ->
        if (rs.failed()) {
          System.err.println("Cannot retrieve the data from the database")
          rs.cause().printStackTrace()
          return
        }

        rs.result().results.each { line ->
          println(groovy.json.JsonOutput.toJson(line))
        }

        // and close the connection
        connection.close({ done ->
          if (done.failed()) {
            throw new java.lang.RuntimeException(done.cause())
          }
        })
      })
    })
  })
})

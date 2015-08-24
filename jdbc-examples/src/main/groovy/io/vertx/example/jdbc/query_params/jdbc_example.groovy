import io.vertx.groovy.ext.jdbc.JDBCClient

def client = JDBCClient.createShared(vertx, [
  url:"jdbc:hsqldb:mem:test?shutdown=true",
  driver_class:"org.hsqldb.jdbcDriver",
  max_pool_size:30
])

client.getConnection({ conn ->
  if (conn.failed()) {
    System.err.println(conn.cause().getMessage())
    return
  }
  def connection = conn.result()

  // create a test table
  connection.execute("create table test(id int primary key, name varchar(255))", { create ->

    // insert some test data
    connection.execute("insert into test values (1, 'Hello'), (2, 'World')", { insert ->

      // query some data with arguments
      connection.queryWithParams("select * from test where id = ?", [
        2
      ], { rs ->
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

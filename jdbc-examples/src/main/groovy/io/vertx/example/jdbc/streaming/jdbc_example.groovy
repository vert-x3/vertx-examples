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
  connection.execute("create table test(id int primary key, name varchar(255))", { res ->
    if (res.failed()) {
      throw new java.lang.RuntimeException(res.cause())
    }
    // insert some test data
    connection.execute("insert into test values (1, 'Hello'), (2, 'Goodbye'), (3, 'Cya Later')", { insert ->
      // query some data
      connection.queryStream("select * from test", { stream ->
        if (stream.succeeded()) {
          def sqlRowStream = stream.result()

          sqlRowStream.handler({ row ->
            // do something with the row...
            println(groovy.json.JsonOutput.toJson(row))
          }).endHandler({ v ->
            // no more data available, close the connection
            connection.close({ done ->
              if (done.failed()) {
                throw new java.lang.RuntimeException(done.cause())
              }
            })
          })
        }
      })
    })
  })
})

import io.vertx.groovy.ext.jdbc.JDBCClient
def query(conn, sql, done) {
  conn.query(sql, { res ->
    if (res.failed()) {
      throw new java.lang.RuntimeException(res.cause())
    }

    done.handle(res.result())
  })
}
def rollbackTx(conn, done) {
  conn.rollback({ res ->
    if (res.failed()) {
      throw new java.lang.RuntimeException(res.cause())
    }

    done.handle(null)
  })
}
def startTx(conn, done) {
  conn.setAutoCommit(false, { res ->
    if (res.failed()) {
      throw new java.lang.RuntimeException(res.cause())
    }

    done.handle(null)
  })
}
def execute(conn, sql, done) {
  conn.execute(sql, { res ->
    if (res.failed()) {
      throw new java.lang.RuntimeException(res.cause())
    }

    done.handle(null)
  })
}

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

  // create a test table
  this.execute(conn.result(), "create table test(id int primary key, name varchar(255))", { create ->
    // start a transaction
    this.startTx(conn.result(), { beginTrans ->
      // insert some test data
      this.execute(conn.result(), "insert into test values(1, 'Hello')", { insert ->
        // commit data
        this.rollbackTx(conn.result(), { rollbackTrans ->
          // query some data
          this.query(conn.result(), "select count(*) from test", { rs ->
            rs.results.each { line ->
              println(groovy.json.JsonOutput.toJson(line))
            }

            // and close the connection
            conn.result().close({ done ->
              if (done.failed()) {
                throw new java.lang.RuntimeException(done.cause())
              }
            })
          })
        })
      })
    })
  })
})

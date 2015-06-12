var JDBCClient = require("vertx-jdbc-js/jdbc_client");
var query = function(conn, sql, done) {
  conn.query(sql, function (res, res_err) {
    if (res_err != null) {
      throw res_err;
    }

    done.handle(res);
  });
};
var startTx = function(conn, done) {
  conn.setAutoCommit(false, function (res, res_err) {
    if (res_err != null) {
      throw res_err;
    }

    done.handle(null);
  });
};
var execute = function(conn, sql, done) {
  conn.execute(sql, function (res, res_err) {
    if (res_err != null) {
      throw res_err;
    }

    done.handle(null);
  });
};
var endTx = function(conn, done) {
  conn.commit(function (res, res_err) {
    if (res_err != null) {
      throw res_err;
    }

    done.handle(null);
  });
};

var client = JDBCClient.createShared(vertx, {
  "url" : "jdbc:hsqldb:mem:test?shutdown=true",
  "driver_class" : "org.hsqldb.jdbcDriver",
  "max_pool_size" : 30
});

client.getConnection(function (conn, conn_err) {
  if (conn_err != null) {
    console.error(conn_err.getMessage());
    return
  }

  // create a test table
  execute(conn, "create table test(id int primary key, name varchar(255))", function (create) {
    // start a transaction
    startTx(conn, function (beginTrans) {
      // insert some test data
      execute(conn, "insert into test values(1, 'Hello')", function (insert) {
        // commit data
        endTx(conn, function (commitTrans) {
          // query some data
          query(conn, "select count(*) from test", function (rs) {
            Array.prototype.forEach.call(rs.results, function(line) {
              console.log(JSON.stringify(line));
            });

            // and close the connection
            conn.close(function (done, done_err) {
              if (done_err != null) {
                throw done_err;
              }
            });
          });
        });
      });
    });
  });
});

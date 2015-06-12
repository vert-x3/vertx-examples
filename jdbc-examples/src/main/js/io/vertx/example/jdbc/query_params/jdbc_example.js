var JDBCClient = require("vertx-jdbc-js/jdbc_client");
var query = function(conn, sql, params, done) {
  conn.queryWithParams(sql, params, function (res, res_err) {
    if (res_err != null) {
      throw res_err;
    }

    done.handle(res);
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
    // insert some test data
    execute(conn, "insert into test values (1, 'Hello'), (2, 'World')", function (insert) {

      // query some data with arguments
      query(conn, "select * from test where id = ?", [
        2
      ], function (rs) {
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

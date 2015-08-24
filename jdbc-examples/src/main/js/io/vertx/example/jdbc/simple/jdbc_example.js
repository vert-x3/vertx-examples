var JDBCClient = require("vertx-jdbc-js/jdbc_client");

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

  var connection = conn;
  connection.execute("create table test(id int primary key, name varchar(255))", function (res, res_err) {
    if (res_err != null) {
      throw res_err;
    }
    // insert some test data
    connection.execute("insert into test values(1, 'Hello')", function (insert, insert_err) {
      // query some data
      connection.query("select * from test", function (rs, rs_err) {
        Array.prototype.forEach.call(rs.results, function(line) {
          console.log(JSON.stringify(line));
        });

        // and close the connection
        connection.close(function (done, done_err) {
          if (done_err != null) {
            throw done_err;
          }
        });
      });
    });
  });
});

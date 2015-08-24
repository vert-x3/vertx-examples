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

  // create a test table
  connection.execute("create table test(id int primary key, name varchar(255))", function (create, create_err) {

    // insert some test data
    connection.execute("insert into test values (1, 'Hello'), (2, 'World')", function (insert, insert_err) {

      // query some data with arguments
      connection.queryWithParams("select * from test where id = ?", [
        2
      ], function (rs, rs_err) {
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

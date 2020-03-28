var PgPool = require("vertx-pg-client-js/pg_pool");

var pool = PgPool.pool(vertx, {
  "port" : 5432,
  "host" : "the-host",
  "database" : "the-db",
  "user" : "user",
  "password" : "secret"
}, {
  "maxSize" : 4
});

// Uncomment for MySQL
//    Pool pool = MySQLPool.pool(vertx, new MySQLConnectOptions()
//      .setPort(5432)
//      .setHost("the-host")
//      .setDatabase("the-db")
//      .setUser("user")
//      .setPassword("secret"), new PoolOptions().setMaxSize(4));

pool.getConnection(function (res1, res1_err) {
  if (res1_err != null) {
    console.error(res1_err.getMessage());
    return
  }
  var connection = res1;

  // create a test table
  connection.query("create table test(id int primary key, name varchar(255))").execute(function (res2, res2_err) {
    if (res2_err != null) {
      connection.close();
      console.error("Cannot create the table");
      res2_err.printStackTrace();
      return
    }

    // insert some test data
    connection.query("insert into test values (1, 'Hello'), (2, 'World')").execute(function (res3, res3_err) {

      // query some data with arguments
      connection.query("select * from test").execute(function (rs, rs_err) {
        if (rs_err != null) {
          console.error("Cannot retrieve the data from the database");
          rs_err.printStackTrace();
          return
        }

        Array.prototype.forEach.call(rs, function(line) {
          console.log("" + line);
        });

        // and close the connection
        connection.close();
      });
    });
  });
});

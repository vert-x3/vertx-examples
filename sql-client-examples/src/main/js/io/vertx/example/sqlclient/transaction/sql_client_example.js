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

pool.begin(function (res1, res1_err) {
  if (res1_err != null) {
    console.error(res1_err.getMessage());
    return
  }
  var tx = res1;

  // create a test table
  tx.query("create table test(id int primary key, name varchar(255))", function (res2, res2_err) {
    if (res2_err != null) {
      tx.close();
      console.error("Cannot create the table");
      res2_err.printStackTrace();
      return
    }

    // insert some test data
    tx.query("insert into test values (1, 'Hello'), (2, 'World')", function (res3, res3_err) {

      // query some data with arguments
      tx.query("select * from test", function (rs, rs_err) {
        if (rs_err != null) {
          console.error("Cannot retrieve the data from the database");
          rs_err.printStackTrace();
          return
        }

        Array.prototype.forEach.call(rs, function(line) {
          console.log("" + line);
        });

        // and close the connection
        tx.commit();
      });
    });
  });
});

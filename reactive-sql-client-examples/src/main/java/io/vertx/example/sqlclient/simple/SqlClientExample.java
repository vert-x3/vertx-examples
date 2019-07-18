package io.vertx.example.sqlclient.simple;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.SqlConnection;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class SqlClientExample extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(SqlClientExample.class);
  }

  @Override
  public void start() throws Exception {

    Pool pool = PgPool.pool(vertx, new PgConnectOptions()
      .setPort(5432)
      .setHost("the-host")
      .setDatabase("the-db")
      .setUser("user")
      .setPassword("secret"), new PoolOptions().setMaxSize(4));

    // Uncomment for MySQL
//    Pool pool = MySQLPool.pool(vertx, new MySQLConnectOptions()
//      .setPort(5432)
//      .setHost("the-host")
//      .setDatabase("the-db")
//      .setUser("user")
//      .setPassword("secret"), new PoolOptions().setMaxSize(4));

    pool.getConnection(res1 -> {
      if (res1.failed()) {
        System.err.println(res1.cause().getMessage());
        return;
      }
      SqlConnection connection = res1.result();

      // create a test table
      connection.query("create table test(id int primary key, name varchar(255))", res2 -> {
        if (res2.failed()) {
          connection.close();
          System.err.println("Cannot create the table");
          res2.cause().printStackTrace();
          return;
        }

        // insert some test data
        connection.query("insert into test values (1, 'Hello'), (2, 'World')", res3 -> {

          // query some data with arguments
          connection.query("select * from test", rs -> {
            if (rs.failed()) {
              System.err.println("Cannot retrieve the data from the database");
              rs.cause().printStackTrace();
              return;
            }

            for (Row line : rs.result()) {
              System.out.println("" + line);
            }

            // and close the connection
            connection.close();
          });
        });
      });
    });
  }
}

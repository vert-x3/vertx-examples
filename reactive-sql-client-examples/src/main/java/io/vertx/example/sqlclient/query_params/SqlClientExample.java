package io.vertx.example.sqlclient.query_params;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class SqlClientExample extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(SqlClientExample.class);
  }

  @Override
  public void start() {

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

    pool.getConnection().compose(connection -> {
      // create a test table
      return connection.query("create table test(id int primary key, name varchar(255))").execute()
        .compose(v -> {
          // insert some test data
          return connection.query("insert into test values (1, 'Hello'), (2, 'World')").execute();
        })
        .compose(v -> {
          // query some data with arguments
          return connection.preparedQuery("select * from test where id = ?").execute(Tuple.of(2));
        })
        .onSuccess(rows -> {
          for (Row row : rows) {
            System.out.println("row = " + row.toString());
          }
        })
        .onComplete(v -> {
          // and close the connection
          connection.close();
        });
    }).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("done");
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}

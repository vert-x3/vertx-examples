package io.vertx.example.sqlclient.transaction;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;

import java.util.ArrayList;
import java.util.List;

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

    pool.withTransaction(sqlClient -> {
      // create a test table
      return sqlClient.query("create table test(id int primary key, name varchar(255))").execute()
        .compose(v -> {
          // insert some test data
          return sqlClient.query("insert into test values (1, 'Hello'), (2, 'World')").execute();
        })
        .compose(v -> {
          // query some data
          return sqlClient.query("select * from test").execute();
        })
        .map(rows -> {
          // convert rows to strings
          List<String> res = new ArrayList<>(rows.size());
          for (Row row : rows) {
            res.add(row.toString());
          }
          return res;
        });
    }).onComplete(ar -> {
      if (ar.succeeded()) {
        List<String> rows = ar.result();
        System.out.println("rows = " + rows);
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}

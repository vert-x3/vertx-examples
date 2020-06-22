package io.vertx.example.sqlclient.streaming;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.example.util.Runner;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.*;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class SqlClientExample extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(io.vertx.example.sqlclient.simple.SqlClientExample.class);
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
      Promise<Void> promise = Promise.promise();
      // create a test table
      connection.query("create table test(id int primary key, name varchar(255))").execute()
        .compose(v -> {
          // insert some test data
          return connection.query("insert into test values (1, 'Hello'), (2, 'World')").execute();
        })
        .compose(v -> {
          // prepare the query
          return connection.prepare("select * from test");
        })
        .map(preparedStatement -> {
          // create a stream
          return preparedStatement.createStream(50, Tuple.tuple());
        })
        .onComplete(ar -> {
          if (ar.succeeded()) {
            RowStream<Row> stream = ar.result();
            stream
              .exceptionHandler(promise::fail)
              .endHandler(promise::complete)
              .handler(row -> System.out.println("row = " + row.toString()));
          } else {
            promise.fail(ar.cause());
          }
        });
      return promise.future().onComplete(v -> {
        // close the connection
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

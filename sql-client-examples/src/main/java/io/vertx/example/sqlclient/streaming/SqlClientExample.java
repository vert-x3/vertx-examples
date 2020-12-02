package io.vertx.example.sqlclient.streaming;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.*;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class SqlClientExample extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>();
    postgres.start();
    PgConnectOptions options = new PgConnectOptions()
      .setPort(postgres.getMappedPort(5432))
      .setHost(postgres.getContainerIpAddress())
      .setDatabase(postgres.getDatabaseName())
      .setUser(postgres.getUsername())
      .setPassword(postgres.getPassword());
    // Uncomment for MySQL
//    MySQLContainer<?> mysql = new MySQLContainer<>();
//    mysql.start();
//    MySQLConnectOptions options = new MySQLConnectOptions()
//      .setPort(mysql.getMappedPort(3306))
//      .setHost(mysql.getContainerIpAddress())
//      .setDatabase(mysql.getDatabaseName())
//      .setUser(mysql.getUsername())
//      .setPassword(mysql.getPassword());
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new io.vertx.example.sqlclient.transaction.SqlClientExample(options));  }

  private final SqlConnectOptions options;

  public SqlClientExample(SqlConnectOptions options) {
    this.options = options;
  }

  @Override
  public void start() {

    Pool pool = Pool.pool(vertx, options, new PoolOptions().setMaxSize(4));

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
              .handler(row -> System.out.println("row = " + row.toJson()));
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

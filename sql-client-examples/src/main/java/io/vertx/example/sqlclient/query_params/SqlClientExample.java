package io.vertx.example.sqlclient.query_params;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.SqlConnectOptions;
import io.vertx.sqlclient.Tuple;
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
    vertx.deployVerticle(new SqlClientExample(options));
  }

  private final SqlConnectOptions options;

  public SqlClientExample(SqlConnectOptions options) {
    this.options = options;
  }

  @Override
  public void start() {

    Pool pool = Pool.pool(vertx, options, new PoolOptions().setMaxSize(4));

    // create a test table
    pool.query("create table test(id int primary key, name varchar(255))")
      .execute()
      .compose(r ->
        // insert some test data
        pool
          .query("insert into test values (1, 'Hello'), (2, 'World')")
          .execute()
      ).compose(r ->
      // query some data with arguments
      pool
        .preparedQuery("select * from test where id = $1")
        .execute(Tuple.of(2))
    ).onSuccess(rows -> {
      for (Row row : rows) {
        System.out.println("row = " + row.toJson());
      }
    }).onFailure(Throwable::printStackTrace);
  }
}

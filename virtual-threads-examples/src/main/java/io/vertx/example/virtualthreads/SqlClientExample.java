package io.vertx.example.virtualthreads;

import io.vertx.core.*;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.*;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.vertx.core.Future.await;

public class SqlClientExample extends AbstractVerticle {

  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();
    try (PostgreSQLContainer<?> container = new PostgreSQLContainer<>()) {
      container.start();
      vertx.deployVerticle(() -> {
          PgConnectOptions connectOptions = new PgConnectOptions()
            .setPort(container.getMappedPort(5432))
            .setHost(container.getContainerIpAddress())
            .setDatabase(container.getDatabaseName())
            .setUser(container.getUsername())
            .setPassword(container.getPassword());
          return new SqlClientExample(Pool.pool(vertx, connectOptions, new PoolOptions().setMaxSize(4)));
        }, new DeploymentOptions()
          .setThreadingModel(ThreadingModel.VIRTUAL_THREAD))
        .toCompletionStage()
        .toCompletableFuture()
        .get();
    }
  }

  private final Pool pool;

  public SqlClientExample(Pool pool) {
    this.pool = pool;
  }

  @Override
  public void start() {
    // create a test table
    await(pool.query("create table test(id int primary key, name varchar(255))").execute());
    // insert some test data
    await(pool.query("insert into test values (1, 'Hello'), (2, 'World')").execute());
    // query some data
    RowSet<Row> rows = await(pool.query("select * from test").execute());
    for (Row row : rows) {
      System.out.println("row = " + row.toJson());
    }
  }
}

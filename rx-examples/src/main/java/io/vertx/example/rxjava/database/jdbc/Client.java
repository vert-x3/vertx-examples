package io.vertx.example.rxjava.database.jdbc;

import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.sql.ResultSet;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Single;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    JsonObject config = new JsonObject().put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver");

    JDBCClient jdbc = JDBCClient.createShared(vertx, config);

    // Connect to the database
    jdbc.rxGetConnection().flatMap(conn -> {

      // Now chain some statements using flatmap composition
      Single<ResultSet> resa = conn.rxUpdate("CREATE TABLE test(col VARCHAR(20))")
        .flatMap(result -> conn.rxUpdate("INSERT INTO test (col) VALUES ('val1')"))
        .flatMap(result -> conn.rxUpdate("INSERT INTO test (col) VALUES ('val2')"))
        .flatMap(result -> conn.rxQuery("SELECT * FROM test"));

      return resa.doAfterTerminate(conn::close);

    }).subscribe(resultSet -> {
      // Subscribe to the final result
      System.out.println("Results : " + resultSet.getRows());
    }, err -> {
      System.out.println("Database problem");
      err.printStackTrace();
    });
  }
}

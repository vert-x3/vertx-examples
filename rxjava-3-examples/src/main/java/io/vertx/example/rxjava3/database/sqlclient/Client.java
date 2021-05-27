package io.vertx.example.rxjava3.database.sqlclient;

import io.reactivex.rxjava3.core.Maybe;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.rxjava3.jdbcclient.JDBCPool;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.sqlclient.Row;
import io.vertx.rxjava3.sqlclient.RowSet;

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

    JDBCPool pool = JDBCPool.pool(vertx, config);

    Maybe<RowSet<Row>> resa = pool.rxWithConnection(conn -> conn
      .query("CREATE TABLE test(col VARCHAR(20))")
      .rxExecute()
      .flatMap(res -> conn.query("INSERT INTO test (col) VALUES ('val1')").rxExecute())
      .flatMap(res -> conn.query("INSERT INTO test (col) VALUES ('val2')").rxExecute())
      .flatMap(res -> conn.query("SELECT * FROM test").rxExecute())
      .toMaybe());

    // Connect to the database
    resa.subscribe(rowSet -> {
      // Subscribe to the final result
      System.out.println("Results:");
      rowSet.forEach(row -> {
        System.out.println(row.toJson());
      });
    }, err -> {
      System.out.println("Database problem");
      err.printStackTrace();
    });
  }
}

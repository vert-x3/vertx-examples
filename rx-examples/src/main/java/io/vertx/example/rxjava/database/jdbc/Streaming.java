package io.vertx.example.rxjava.database.jdbc;

import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.jdbc.JDBCClient;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Streaming extends AbstractVerticle {

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
    jdbc.rxGetConnection().subscribe(
      conn -> {

        conn.rxUpdate("CREATE TABLE test(col VARCHAR(20))").
          flatMap(result -> conn.rxUpdate("INSERT INTO test (col) VALUES ('val1')")).
          flatMap(result -> conn.rxUpdate("INSERT INTO test (col) VALUES ('val2')")).
          flatMap(result -> conn.rxQueryStream("SELECT * FROM test")).subscribe(
            rowStream -> {
              rowStream.toObservable().subscribe(
                row -> System.out.println("Row : " + row.encode()),
                Throwable::printStackTrace,
                conn::close
              );
            },
          Throwable::printStackTrace);
      },

      // Could not connect
      Throwable::printStackTrace
    );

  }
}

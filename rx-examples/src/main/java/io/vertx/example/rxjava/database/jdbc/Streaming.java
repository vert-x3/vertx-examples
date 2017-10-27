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
    Runner.runExample(Streaming.class);
  }

  @Override
  public void start() throws Exception {

    JsonObject config = new JsonObject().put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver");

    JDBCClient jdbc = JDBCClient.createShared(vertx, config);

    jdbc
      .rxGetConnection() // Connect to the database
      .flatMapObservable(conn -> { // With the connection...
        return conn.rxUpdate("CREATE TABLE test(col VARCHAR(20))") // ...create test table
          .flatMap(result -> conn.rxUpdate("INSERT INTO test (col) VALUES ('val1')")) // ...insert a row
          .flatMap(result -> conn.rxUpdate("INSERT INTO test (col) VALUES ('val2')")) // ...another one
          .flatMap(result -> conn.rxQueryStream("SELECT * FROM test")) // ...get values stream
          .flatMapObservable(sqlRowStream -> {
            return sqlRowStream.toObservable() // Transform the stream into an Observable...
              .doOnTerminate(conn::close); // ...and close the connection when the stream is fully read or an error occurs
          });
      }).subscribe(row -> System.out.println("Row : " + row.encode()));
  }
}

package io.vertx.example.reactivex.database.sqlclient;

import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.jdbc.JDBCClient;

/*
 * @author <a href="mailto:plopes@redhat.com">Paulo Lopes</a>
 */
public class Streaming extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Streaming.class.getName());
  }

  @Override
  public void start() throws Exception {

    JsonObject config = new JsonObject().put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver");

    JDBCClient jdbc = JDBCClient.createShared(vertx, config);

    jdbc
      .rxGetConnection() // Connect to the database
      .flatMapPublisher(conn -> { // With the connection...
        return conn.rxUpdate("CREATE TABLE test(col VARCHAR(20))") // ...create test table
          .flatMap(result -> conn.rxUpdate("INSERT INTO test (col) VALUES ('val1')")) // ...insert a row
          .flatMap(result -> conn.rxUpdate("INSERT INTO test (col) VALUES ('val2')")) // ...another one
          .flatMap(result -> conn.rxQueryStream("SELECT * FROM test")) // ...get values stream
          .flatMapPublisher(sqlRowStream -> {
            return sqlRowStream.toFlowable() // Transform the stream into a Flowable...
              .doOnTerminate(() -> {
                // ...and close the connection when the stream is fully read or an
                // error occurs
                conn.close();
                System.out.println("Connection closed");
              });
          });
      }).subscribe(row -> System.out.println("Row : " + row.encode()));
  }
}

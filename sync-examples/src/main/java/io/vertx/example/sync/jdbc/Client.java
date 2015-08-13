package io.vertx.example.sync.jdbc;

import co.paralleluniverse.fibers.Suspendable;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.sync.SyncVerticle;
import static io.vertx.ext.sync.Sync.*;


/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Client extends SyncVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  @Suspendable
  public void start() throws Exception {

    JsonObject config = new JsonObject().put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver");

    JDBCClient jdbc = JDBCClient.createShared(vertx, config);

    // Get a connection

    try (SQLConnection conn = syncResult(jdbc::getConnection)) {

      // Create a table
      Void v = syncResult(h -> conn.execute("CREATE TABLE test(col VARCHAR(20))", h));

      // Insert some stuff
      for (int i = 0; i < 10; i++) {
        int ii = i;
        UpdateResult res = syncResult(h -> conn.update("INSERT INTO test (col) VALUES ('val" + ii + "')", h));
        System.out.println("Rows updated: " + res.getUpdated());
      }

      // Select the results
      ResultSet res = syncResult(h -> conn.query("SELECT * FROM test", h));
      System.out.println("Selected " + res.getNumRows() + " results");

      res.getResults().forEach(System.out::println);

    }

  }

}

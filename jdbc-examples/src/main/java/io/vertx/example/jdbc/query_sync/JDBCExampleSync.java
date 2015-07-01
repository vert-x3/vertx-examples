package io.vertx.example.jdbc.query_sync;

import co.paralleluniverse.fibers.Suspendable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sync.SyncVerticle;
import io.vertx.sync.ext.jdbc.JDBCClientSync;
import io.vertx.sync.ext.sql.SQLConnectionSync;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class JDBCExampleSync extends SyncVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(JDBCExampleSync.class);
  }

  // Note - start method MUST be marked as Suspendable
  @Override
  @Suspendable
  public void start() {

    JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
        .put("driver_class", "org.hsqldb.jdbcDriver")
        .put("max_pool_size", 30));

    JDBCClientSync clientSync = new JDBCClientSync(client);
    SQLConnectionSync conn = clientSync.getConnectionSync();
    try {
      conn.executeSync("create table test(id int primary key, name varchar(255))");
      conn.executeSync("insert into test values (1, 'Hello'), (2, 'World')");
      ResultSet rs = conn.queryWithParamsSync("select * from test where id = ?", new JsonArray().add(2));
      for (JsonArray line : rs.getResults()) {
        System.out.println(line.encode());
      }
    } finally {
      conn.closeSync();
    }
  }
}

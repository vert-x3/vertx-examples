package io.vertx.example.query_sync;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.sync.ext.jdbc.JDBCClientSync;
import io.vertx.sync.ext.sql.SQLConnectionSync;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class JDBCExampleSync extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(JDBCExampleSync.class);
  }

  @Override
  public void start() throws Exception {

    final JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
        .put("driver_class", "org.hsqldb.jdbcDriver")
        .put("max_pool_size", 30));

    JDBCClientSync clientSync = new JDBCClientSync(client);
    SQLConnection conn = clientSync.getConnectionSync();
    SQLConnectionSync syncConnection = new SQLConnectionSync(conn);
    syncConnection.executeSync("create table test(id int primary key, name varchar(255))");
    syncConnection.executeSync("insert into test values (1, 'Hello'), (2, 'World')");
    ResultSet rs = syncConnection.queryWithParamsSync("select * from test where id = ?", new JsonArray().add(2));
    for (JsonArray line : rs.getResults()) {
      System.out.println(line.encode());
    }
    syncConnection.closeSync();
  }
}

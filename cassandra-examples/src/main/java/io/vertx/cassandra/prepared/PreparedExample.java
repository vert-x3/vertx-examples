package io.vertx.cassandra.prepared;

import com.datastax.driver.core.PreparedStatement;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

public class PreparedExample extends AbstractVerticle {

  /**
   * Convenience method so you can run it in your IDE
   */
  public static void main(String[] args) {
    Launcher.main(new String[]{"run", PreparedExample.class.getName()});
  }

  @Override
  public void start() {
    CassandraClient client = CassandraClient.createNonShared(vertx, new CassandraClientOptions().setPort(9042));
    client.prepare("SELECT * from system_schema.tables  WHERE keyspace_name = ? ", preparedStatementAsyncResult -> {
      if (preparedStatementAsyncResult.succeeded()) {
        System.out.println("The query has successfully been prepared");
        PreparedStatement preparedStatement = preparedStatementAsyncResult.result();
        client.executeWithFullFetch(preparedStatement.bind("system_schema"), listAsyncResult -> {
          if (listAsyncResult.succeeded()) {
            System.out.println("Tables in system_schema: ");
            listAsyncResult.result().forEach(row -> {
              String systemSchema = row.getString("table_name");
              System.out.println("\t" + systemSchema);
            });
          } else {
            System.out.println("Unable to execute a prepared statement");
            listAsyncResult.cause().printStackTrace();
          }
        });
      } else {
        System.out.println("Unable to prepare the query");
        preparedStatementAsyncResult.cause().printStackTrace();
      }
    });
  }
}

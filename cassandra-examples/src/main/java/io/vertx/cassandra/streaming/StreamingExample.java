package io.vertx.cassandra.streaming;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.cassandra.CassandraRowStream;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

public class StreamingExample extends AbstractVerticle {


  /**
   * Convenience method so you can run it in your IDE
   */
  public static void main(String[] args) {
    Launcher.main(new String[]{"run", StreamingExample.class.getName()});
  }

  @Override
  public void start() {
    CassandraClient client = CassandraClient.createNonShared(vertx, new CassandraClientOptions().setPort(9042));
    client.queryStream("SELECT * from system_schema.tables  WHERE keyspace_name = 'system_schema' ", cassandraRowStreamAsyncResult -> {
      if (cassandraRowStreamAsyncResult.succeeded()) {
        System.out.println("Tables in system_schema: ");
        CassandraRowStream stream = cassandraRowStreamAsyncResult.result();
        stream
          .endHandler(end -> {
            System.out.println("<End of Cassandra row stream>");
          })
          .handler(row -> {
            String systemSchema = row.getString("table_name");
            System.out.println("\t" + systemSchema);
          })
          .exceptionHandler(throwable -> {
            System.out.println("An exception occurred:");
            throwable.printStackTrace();
          });
      } else {
        System.out.println("Unable to execute the query");
        cassandraRowStreamAsyncResult.cause().printStackTrace();
      }
    });
  }
}

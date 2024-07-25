package io.vertx.example.cassandra.streaming;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.cassandra.CassandraRowStream;
import io.vertx.core.AbstractVerticle;
import io.vertx.launcher.application.VertxApplication;

public class StreamingExample extends AbstractVerticle {


  /**
   * Convenience method so you can run it in your IDE
   */
  public static void main(String[] args) {
    VertxApplication.main(new String[]{StreamingExample.class.getName()});
  }

  @Override
  public void start() {
    CassandraClient client = CassandraClient.createShared(vertx, new CassandraClientOptions());
    client.queryStream("SELECT * from system_schema.tables  WHERE keyspace_name = 'system_schema' ")
      .onComplete(cassandraRowStreamAsyncResult -> {
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

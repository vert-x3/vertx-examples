package io.vertx.example.cassandra.streaming;

import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.cassandra.CassandraRowStream;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.launcher.application.VertxApplication;

public class StreamingExample extends VerticleBase {


  /**
   * Convenience method so you can run it in your IDE
   */
  public static void main(String[] args) {
    VertxApplication.main(new String[]{StreamingExample.class.getName()});
  }

  private CassandraClient client;

  @Override
  public Future<?> start() {
    client = CassandraClient.createShared(vertx, new CassandraClientOptions());
    return Future.future(p -> {
      client
        .queryStream("SELECT * from system_schema.tables  WHERE keyspace_name = 'system_schema' ")
        .onComplete(cassandraRowStreamAsyncResult -> {
          if (cassandraRowStreamAsyncResult.succeeded()) {
            System.out.println("Tables in system_schema: ");
            CassandraRowStream stream = cassandraRowStreamAsyncResult.result();
            stream
              .endHandler(end -> {
                p.tryComplete();
              })
              .handler(row -> {
                String systemSchema = row.getString("table_name");
                System.out.println("\t" + systemSchema);
              })
              .exceptionHandler(throwable -> {
                System.out.println("An exception occurred:");
                p.tryFail(cassandraRowStreamAsyncResult.cause());
              });
          } else {
            System.out.println("Unable to execute the query");
            p.tryFail(cassandraRowStreamAsyncResult.cause());
          }
        });
    });
  }
}

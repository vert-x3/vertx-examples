package io.vertx.example.cassandra.prepared;

import io.vertx.cassandra.CassandraClient;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.launcher.application.VertxApplication;

public class PreparedExample extends VerticleBase {

  /**
   * Convenience method so you can run it in your IDE
   */
  public static void main(String[] args) {
    VertxApplication.main(new String[]{PreparedExample.class.getName()});
  }

  private CassandraClient client;

  @Override
  public Future<?> start() {
    client = CassandraClient.createShared(vertx);

    return client
      .prepare("SELECT * from system_schema.tables  WHERE keyspace_name = ? ")
      .compose(preparedStatement -> client.executeWithFullFetch(preparedStatement.bind("system_schema")))
      .onSuccess(res -> {
        System.out.println("Tables in system_schema: ");
        res.forEach(row -> {
          String systemSchema = row.getString("table_name");
          System.out.println("\t" + systemSchema);
        });
    });
  }
}

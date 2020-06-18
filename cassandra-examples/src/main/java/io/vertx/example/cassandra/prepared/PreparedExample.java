package io.vertx.example.cassandra.prepared;

import io.vertx.cassandra.CassandraClient;
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
    CassandraClient client = CassandraClient.createShared(vertx);
    client.prepare("SELECT * from system_schema.tables  WHERE keyspace_name = ? ").compose(preparedStatement -> {
      return client.executeWithFullFetch(preparedStatement.bind("system_schema"));
    }).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("Tables in system_schema: ");
        ar.result().forEach(row -> {
          String systemSchema = row.getString("table_name");
          System.out.println("\t" + systemSchema);
        });
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}

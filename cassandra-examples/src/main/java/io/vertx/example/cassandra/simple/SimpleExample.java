package io.vertx.example.cassandra.simple;

import com.datastax.oss.driver.api.core.cql.Row;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

public class SimpleExample extends AbstractVerticle {

  /**
   * Convenience method so you can run it in your IDE
   */
  public static void main(String[] args) {
    Launcher.main(new String[]{"run", SimpleExample.class.getName()});
  }

  @Override
  public void start() {
    CassandraClient client = CassandraClient.createShared(vertx);
    client.execute("select release_version from system.local").onComplete(ar -> {
      if (ar.succeeded()) {
        Row row = ar.result().one();
        String releaseVersion = row.getString("release_version");
        System.out.println("Release version is: " + releaseVersion);
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}

package io.vertx.cassandra.simple;

import com.datastax.driver.core.Row;
import io.vertx.cassandra.CassandraClient;
import io.vertx.cassandra.CassandraClientOptions;
import io.vertx.cassandra.ResultSet;
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
    CassandraClient client = CassandraClient.createNonShared(vertx, new CassandraClientOptions().setPort(9042));
    client.execute("select release_version from system.local", rs -> {
      if (rs.succeeded()) {
        ResultSet result = rs.result();
        result.one(one -> {
          if (one.succeeded()) {
            Row row = one.result();
            String releaseVersion = row.getString("release_version");
            System.out.println("Release version is: "+ releaseVersion);
          } else {
            one.cause().printStackTrace();
          }
        });
      } else {
        rs.cause().printStackTrace();
      }
    });
  }
}

package io.vertx.example.cassandra.simple;

import com.datastax.oss.driver.api.core.cql.Row;
import io.vertx.cassandra.CassandraClient;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.launcher.application.VertxApplication;

public class SimpleExample extends VerticleBase {

  /**
   * Convenience method so you can run it in your IDE
   */
  public static void main(String[] args) {
    VertxApplication.main(new String[]{SimpleExample.class.getName()});
  }

  @Override
  public Future<?> start() {
    CassandraClient client = CassandraClient.createShared(vertx);
    return client.execute("select release_version from system.local")
      .onSuccess(res -> {
        Row row = res.one();
        String releaseVersion = row.getString("release_version");
        System.out.println("Release version is: " + releaseVersion);
    });
  }
}

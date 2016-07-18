package io.vertx.example.osgi.jdbc;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.example.osgi.TcclSwitch;
import io.vertx.ext.jdbc.JDBCClient;
import org.apache.felix.ipojo.annotations.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

@Component
@Instantiate
public class JDBCClientProvider {

  @Context
  private BundleContext context;

  @Requires(proxy = false)
  private Vertx vertx;

  private ServiceRegistration<JDBCClient> registration;
  private JDBCClient client;

  @Validate
  public void registerJDBCClientService() throws Exception {
    client = TcclSwitch.executeWithTCCLSwitch(() ->
        JDBCClient.createShared(vertx, new JsonObject()
            .put("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariCPDataSourceProvider")
            .put("jdbcUrl", "jdbc:hsqldb:mem:test?shutdown=true")
            .put("driverClassName", "org.hsqldb.jdbcDriver")
            .put("maximumPoolSize", 30)));
    registration = context.registerService(JDBCClient.class, client, null);
  }

  @Invalidate
  public void unregisterJDBCClientService() {
    registration.unregister();
    client.close();
  }
}

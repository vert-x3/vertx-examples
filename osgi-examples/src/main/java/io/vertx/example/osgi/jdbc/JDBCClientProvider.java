package io.vertx.example.osgi.jdbc;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import org.apache.felix.ipojo.annotations.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@Component
@Instantiate
public class JDBCClientProvider {

  @Context
  private BundleContext context;

  @Requires
  private Vertx vertx;

  private ServiceRegistration<JDBCClient> registration;
  private JDBCClient client;

  @Validate
  public void registerJDBCClientService() {
    client = JDBCClient.createShared(vertx, new JsonObject()
        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
        .put("driver_class", "org.hsqldb.jdbcDriver")
        .put("max_pool_size", 30));
    registration = context.registerService(JDBCClient.class, client, null);
  }

  @Invalidate
  public void unregisterJDBCClientService() {
    registration.unregister();
    client.close();
  }
}

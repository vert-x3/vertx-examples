package io.vertx.example.rx.database.jdbc;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.sql.ResultSet;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.jdbc.JdbcService;
import rx.Observable;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  @Override
  public void start() throws Exception {

    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(new JsonObject()
        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
        .put("driver_class", "org.hsqldb.jdbcDriver"));

    vertx.deployVerticle("service:io.vertx:jdbc-service", options, res -> {
      if (res.succeeded()) {

        // Create a proxy
        JdbcService proxy = JdbcService.createEventBusProxy(vertx, "vertx.jdbc");

        // Connect to the database
        proxy.getConnectionObservable().subscribe(
            conn -> {

              // Now chain some statements using flatmap composition
              Observable<ResultSet> resa = conn.updateObservable("CREATE TABLE test(col VARCHAR(20))").
                  flatMap(result -> conn.updateObservable("INSERT INTO test (col) VALUES ('val1')")).
                  flatMap(result -> conn.updateObservable("INSERT INTO test (col) VALUES ('val2')")).
                  flatMap(result -> conn.queryObservable("SELECT * FROM test"));

              // Subscribe to the final result
              resa.subscribe(resultSet -> {
                System.out.println("Results : " + resultSet.getRows());
              }, err -> {
                System.out.println("Database problem");
                err.printStackTrace();
              });
            },

            // Could not connect
            err -> {
              System.out.println("Could not connect to database");
              err.printStackTrace();
            }
        );
      } else {
        res.cause().printStackTrace();
      }
    });
  }
}

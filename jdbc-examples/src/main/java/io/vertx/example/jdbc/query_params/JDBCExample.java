package io.vertx.example.jdbc.query_params;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class JDBCExample extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(JDBCExample.class);
  }

  @Override
  public void start() throws Exception {

    final JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
        .put("driver_class", "org.hsqldb.jdbcDriver")
        .put("max_pool_size", 30));

    client.getConnection(conn -> {
      if (conn.failed()) {
        System.err.println(conn.cause().getMessage());
        return;
      }
      final SQLConnection connection = conn.result();

      // create a test table
      connection.execute("create table test(id int primary key, name varchar(255))", create -> {
        if (create.failed()) {
          System.err.println("Cannot create the table");
          create.cause().printStackTrace();
          return;
        }

        // insert some test data
        connection.execute("insert into test values (1, 'Hello'), (2, 'World')", insert -> {

          // query some data with arguments
          connection.queryWithParams("select * from test where id = ?", new JsonArray().add(2), rs -> {
            if (rs.failed()) {
              System.err.println("Cannot retrieve the data from the database");
              rs.cause().printStackTrace();
              return;
            }

            for (JsonArray line : rs.result().getResults()) {
              System.out.println(line.encode());
            }

            // and close the connection
            connection.close(done -> {
              if (done.failed()) {
                throw new RuntimeException(done.cause());
              }
            });
          });
        });
      });
    });
  }
}

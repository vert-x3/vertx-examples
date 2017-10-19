package io.vertx.example.java9.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class JDBCExample extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new JDBCExample(), ar -> {
      if (ar.failed()) {
        ar.cause().printStackTrace();
      }
    });
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
      connection.execute("create table test(id int primary key, name varchar(255))", res -> {
        if (res.failed()) {
          throw new RuntimeException(res.cause());
        }
        // insert some test data
        connection.execute("insert into test values(1, 'Hello')", insert -> {
          // query some data
          connection.query("select * from test", rs -> {
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

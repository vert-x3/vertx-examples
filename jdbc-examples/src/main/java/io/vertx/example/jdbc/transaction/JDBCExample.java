package io.vertx.example.jdbc.transaction;

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

      // create a test table
      execute(conn.result(), "create table test(id int primary key, name varchar(255))", create -> {
        // start a transaction
        startTx(conn.result(), beginTrans -> {
          // insert some test data
          execute(conn.result(), "insert into test values(1, 'Hello')", insert -> {
            // commit data
            endTx(conn.result(), commitTrans -> {
              // query some data
              query(conn.result(), "select count(*) from test", rs -> {
                for (JsonArray line : rs.getResults()) {
                  System.out.println(line.encode());
                }

                // and close the connection
                conn.result().close(done -> {
                  if (done.failed()) {
                    throw new RuntimeException(done.cause());
                  }
                });
              });
            });
          });
        });
      });
    });
  }

  private void execute(SQLConnection conn, String sql, Handler<Void> done) {
    conn.execute(sql, res -> {
      if (res.failed()) {
        throw new RuntimeException(res.cause());
      }

      done.handle(null);
    });
  }

  private void query(SQLConnection conn, String sql, Handler<ResultSet> done) {
    conn.query(sql, res -> {
      if (res.failed()) {
        throw new RuntimeException(res.cause());
      }

      done.handle(res.result());
    });
  }

  private void startTx(SQLConnection conn, Handler<ResultSet> done) {
    conn.setAutoCommit(false, res -> {
      if (res.failed()) {
        throw new RuntimeException(res.cause());
      }

      done.handle(null);
    });
  }

  private void endTx(SQLConnection conn, Handler<ResultSet> done) {
    conn.commit(res -> {
      if (res.failed()) {
        throw new RuntimeException(res.cause());
      }

      done.handle(null);
    });
  }
}

package io.vertx.example.osgi.jdbc;

import io.vertx.core.*;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.example.osgi.service.DataService;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import org.apache.felix.ipojo.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Provides
@Instantiate
public class JDBCServiceConsumer implements DataService {

  @Requires
  private JDBCClient client;

  @Validate
  public void start() {

  }

  @Override
  public void retrieve(Handler<AsyncResult<List<String>>> resultHandler) {
    client.getConnection(conn -> {
      if (conn.failed()) {
        resultHandler.handle(Future.failedFuture(conn.cause()));
        return;
      }

      final SQLConnection connection = conn.result();
      connection.execute("create table test(id int primary key, name varchar(255))", res -> {
        if (res.failed()) {
          resultHandler.handle(Future.failedFuture(res.cause()));
          return;
        }
        // insert some test data
        List<String> results = new ArrayList<>();
        connection.execute("insert into test values(1, 'Hello')", insert -> {
          // query some data
          connection.query("select * from test", rs -> {
            results.addAll(rs.result().getResults().stream().map(JsonArray::encode).collect(Collectors.toList()));

            // and close the connection
            connection.close(done -> {
              if (done.failed()) {
                resultHandler.handle(Future.failedFuture(done.cause()));
              } else {
                resultHandler.handle(Future.succeededFuture(results));
              }
            });
          });
        });
      });
    });
  }
}

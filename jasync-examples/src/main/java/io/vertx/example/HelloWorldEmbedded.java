package io.vertx.example;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;


/**
 * @author oshai
 */
public class HelloWorldEmbedded {

  public static void main(String[] args) {
    JsonObject mySQLClientConfig = new JsonObject()
            .put("username", "mysql_async")
            .put("password", "root")
            .put("database", "mysql_async_tests")
            .put("port", 33018)
            .put("host", "localhost");
    SQLClient mySQLClient = MySQLClient.createShared(Vertx.vertx(), mySQLClientConfig);

    // Create an HTTP server which simply returns "Hello World!" to each request.
    Vertx.vertx().createHttpServer()
            .requestHandler(req -> {
              mySQLClient.getConnection(res -> {
                if (res.succeeded()) {

                  SQLConnection connection = res.result();

                  // Got a connection
                  connection.query("SELECT 0", result ->
                          req.response().end("Got response " + result.result().getNumRows()));
                } else {
                  // Failed to get connection - deal with it
                  req.response().end("Failed!");
                }
              });

            })
            .listen(8080);
  }

}

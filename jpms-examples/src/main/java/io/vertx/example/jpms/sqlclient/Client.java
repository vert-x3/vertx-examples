package io.vertx.example.jpms.sqlclient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Client extends AbstractVerticle {

  private PgConnectOptions database;
  private HttpServer server;
  private Pool client;

  // Tested with test containers
  public Client(PgConnectOptions database) {
    this.database = database;
  }

  @Override
  public void start(Promise<Void> start) {
    client = PgBuilder.pool()
      .connectingTo(database)
      .using(vertx)
      .build();

    server = vertx.createHttpServer()
      .requestHandler(req -> {
        client.withConnection(conn -> conn
          .query("SELECT * FROM periodic_table")
          .collecting(Collectors.mapping(row -> row.toJson(), Collectors.toList()))
          .execute()).onComplete(ar -> {
          if (ar.succeeded()) {
            req
              .response()
              .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
              .end(new JsonArray(ar.result().value()).encode());
          } else {
            req.response()
              .setStatusCode(500)
              .end(ar.cause().toString());
          }
        });
      });

    server.listen(8080)
      .<Void>mapEmpty()
      .onComplete(start);
  }
}

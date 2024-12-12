package io.vertx.example.jpms.sqltemplate;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.Collections;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class ClientWithTemplate extends VerticleBase {

  private PgConnectOptions database;
  private HttpServer server;
  private Pool client;

  // Tested with test containers
  public ClientWithTemplate(PgConnectOptions database) {
    this.database = database;
  }

  public Future<?> start() {
    client = PgBuilder.pool()
      .connectingTo(database)
      .using(vertx)
      .build();

    server = vertx.createHttpServer()
      .requestHandler(req -> {
        SqlTemplate
          .forQuery(client, "SELECT * FROM periodic_table")
          .mapTo(ElementOfPeriodicTableRowMapper.INSTANCE)
          .execute(Collections.emptyMap())
          .map(elements -> {
            JsonArray res = new JsonArray();
            elements.forEach(element -> {
              res.add(new JsonObject().put("Element", element.getElement()).put("Symbol", element.getSymbol()));
            });
            return res;
          })
          .onComplete(ar -> {
            if (ar.succeeded()) {
              req
                .response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end(ar.result().encode());
            } else {
              req.response()
                .setStatusCode(500)
                .end(ar.cause().toString());
            }
          });
      });

    return server.listen(8080);
  }
}

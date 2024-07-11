package io.vertx.example.jpms.sqlclient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

/*
 * @author <a href="mailto:pmlopes@gmail.com">Paulo Lopes</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new Client())
      .onFailure(Throwable::printStackTrace);
  }

  @Override
  public void start() {

    final Pool client = JDBCPool.pool(vertx,
      new JDBCConnectOptions().setJdbcUrl("jdbc:hsqldb:mem:test?shutdown=true"),
      new PoolOptions().setMaxSize(30));


    client.withConnection(conn -> conn
      .query("create table test(id int primary key, name varchar(255))")
      .execute()
      .compose(res -> conn
        // insert some test data
        .query("insert into test values(1, 'Hello')")
        .execute())
      .compose(res -> conn
        // query some data
        .query("select * from test")
        .execute()))
      .onSuccess(rows -> {
        rows.forEach(row -> {
          System.out.println(row.toJson().encode());
        });
      }).onFailure(Throwable::printStackTrace);
  }
}

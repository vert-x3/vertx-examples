package io.vertx.example.tracing;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;

public class ChuckNorrisJokesVerticle extends AbstractVerticle {

  private PgConnectOptions options;
  private PgPool pool;

  public ChuckNorrisJokesVerticle(PgConnectOptions options) {
    this.options = options;
  }

  @Override
  public void start(Promise<Void> startPromise) {

    pool = PgPool.pool(vertx, options, new PoolOptions().setMaxSize(5));

    pool.query("create table jokes(joke varchar(255))").execute()
      .compose(v -> vertx.fileSystem().readFile("jokes.json"))
      .compose(buffer -> {
        JsonArray array = new JsonArray(buffer);
        List<Tuple> batch = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
          String joke = array.getJsonObject(i).getString("joke");
          batch.add(Tuple.of(joke));
        }
        return pool
          .preparedQuery("insert into jokes values ($1)")
          .executeBatch(batch);
      })
      .<Void>mapEmpty()
      .onComplete(startPromise);

    vertx.createHttpServer().requestHandler(req -> {

      pool
        .query("select joke from jokes ORDER BY random() limit 1")
        .execute().onComplete(res -> {
          if (res.succeeded() && res.result().size() > 0) {
            Row row = res.result().iterator().next();
            String joke = row.getString(0);
            req.response().putHeader("content-type", "text/plain").end(joke);
          } else {
            req.response().setStatusCode(500).end("No jokes available");
          }
      });
    }).listen(8082);
  }
}

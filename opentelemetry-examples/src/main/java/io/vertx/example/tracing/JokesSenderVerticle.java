package io.vertx.example.tracing;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.eventbus.EventBus;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;

public class JokesSenderVerticle extends VerticleBase {

  private final PgConnectOptions options;
  private Pool pool;

  public JokesSenderVerticle(PgConnectOptions options) {
    this.options = options;
  }

  @Override
  public Future<?> start() throws Exception {
    pool = PgBuilder.pool()
      .with(new PoolOptions().setMaxSize(5))
      .connectingTo(options)
      .using(vertx)
      .build();

    EventBus eb = vertx.eventBus();

    eb.consumer("ping-address", message -> {
      pool
        .query("select joke from jokes ORDER BY random() limit 1")
        .execute().onComplete(res -> {
          if (res.succeeded() && res.result().size() > 0) {
            Row row = res.result().iterator().next();
            String joke = row.getString(0);
            message.reply(joke);
          } else {
            message.fail(500, "No jokes available");
          }
        });
    });

    System.out.println("Receiver ready!");
    return super.start();
  }
}

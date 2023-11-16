package io.vertx.example.virtualthreads;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.*;

import java.util.Arrays;
import java.util.List;

import static io.vertx.core.Future.await;

public class MovieRatingService extends AbstractVerticle {

  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MovieRatingService.class, new DeploymentOptions().setThreadingModel(ThreadingModel.VIRTUAL_THREAD))
      .toCompletionStage()
      .toCompletableFuture()
      .get();
  }

  private static final List<String> DB_STATEMENTS = Arrays.asList(
    "CREATE TABLE MOVIE (ID VARCHAR(16) PRIMARY KEY, TITLE VARCHAR(256) NOT NULL)",
    "CREATE TABLE RATING (ID INTEGER IDENTITY PRIMARY KEY, value INTEGER, MOVIE_ID VARCHAR(16))",
    "INSERT INTO MOVIE (ID, TITLE) VALUES 'starwars', 'Star Wars'",
    "INSERT INTO MOVIE (ID, TITLE) VALUES 'indianajones', 'Indiana Jones'",
    "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 1, 'starwars'",
    "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 5, 'starwars'",
    "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 9, 'starwars'",
    "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 10, 'starwars'",
    "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 4, 'indianajones'",
    "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 7, 'indianajones'",
    "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 3, 'indianajones'",
    "INSERT INTO RATING (VALUE, MOVIE_ID) VALUES 9, 'indianajones'"
  );

  private SqlClient client;

  @Override
  public void start() {
    client = JDBCPool.pool(vertx, new JsonObject()
      .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver")
      .put("max_pool_size-loop", 30)
    );

    for (String statement : DB_STATEMENTS) {
      await(client.query(statement).execute());
    }

    // Build Vert.x Web router
    Router router = Router.router(vertx);
    router.get("/movie/:id").handler(this::getMovie);
    router.get("/rateMovie/:id").handler(this::rateMovie);
    router.get("/getRating/:id").handler(this::getRating);

    // Start the server
    await(vertx.createHttpServer()
      .requestHandler(router)
      .listen(config().getInteger("http.port", 8080)));
  }

  // Send info about a movie
  private void getMovie(RoutingContext ctx) {
    String id = ctx.pathParam("id");
    RowSet<Row> rows = await(client.preparedQuery("SELECT TITLE FROM MOVIE WHERE ID=?").execute(Tuple.of(id)));
    if (rows.size() == 1) {
      ctx.response()
        .putHeader("Content-Type", "application/json")
        .end(new JsonObject()
          .put("id", id)
          .put("title", rows.iterator().next().getString("TITLE"))
          .encode());
    } else {
      ctx.response().setStatusCode(404).end();
    }
  }

  // Rate a movie
  private void rateMovie(RoutingContext ctx) {
    String movie = ctx.pathParam("id");
    int rating = Integer.parseInt(ctx.queryParam("rating").get(0));
    RowSet<Row> rows = await(client.preparedQuery("SELECT TITLE FROM MOVIE WHERE ID=?").execute(Tuple.of(movie)));
    if (rows.size() == 1) {
      await(client.preparedQuery("INSERT INTO RATING (VALUE, MOVIE_ID) VALUES ?, ?").execute(Tuple.of(rating, movie)));
      ctx.response().setStatusCode(200).end();
    } else {
      ctx.response().setStatusCode(404).end();
    }
  }

  // Get the current rating of a movie
  private void getRating(RoutingContext ctx) {
    String id = ctx.pathParam("id");
    RowSet<Row> rows = await(client.preparedQuery("SELECT AVG(VALUE) AS VALUE FROM RATING WHERE MOVIE_ID=?").execute(Tuple.of(id)));
    ctx.response()
      .putHeader("Content-Type", "application/json")
      .end(new JsonObject()
        .put("id", id)
        .put("title", rows.iterator().next().getDouble("VALUE"))
        .encode());
  }
}

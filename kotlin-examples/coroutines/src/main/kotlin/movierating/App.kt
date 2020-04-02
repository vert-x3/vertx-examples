package movierating

import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.kotlin.sqlclient.executeAwait
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.*
import kotlinx.coroutines.launch


class App : CoroutineVerticle() {

  private lateinit var client: MySQLPool
  private lateinit var getTitleByMovieId : PreparedQuery<RowSet<Row>>
  private lateinit var insertRatingWithMovieId : PreparedQuery<RowSet<Row>>
  private lateinit var getAverageRatingByMovieId : PreparedQuery<RowSet<Row>>

  override suspend fun start() {

    client = MySQLPool.pool(vertx, MySQLConnectOptions.fromUri("mysql://vertx:vertx@localhost:3306/vertx_example")
      .setCachePreparedStatements(true)
      .setPreparedStatementCacheMaxSize(1024), PoolOptions().setMaxSize(50))

    // Populate database
    client.query("""
      DROP TABLE IF EXISTS movie;
      DROP TABLE IF EXISTS rating;
      CREATE TABLE movie (id VARCHAR(16) PRIMARY KEY, title VARCHAR(256) NOT NULL);
      CREATE TABLE rating (id INTEGER AUTO_INCREMENT PRIMARY KEY, `value` INTEGER, movie_id VARCHAR(16));
      INSERT INTO movie (id, title) VALUES ('starwars', 'Star Wars');
      INSERT INTO movie (id, title) VALUES ('indianajones', 'Indiana Jones');
      INSERT INTO rating (`value`, movie_id) VALUES (1, 'starwars');
      INSERT INTO rating (`value`, movie_id) VALUES (5, 'starwars');
      INSERT INTO rating (`value`, movie_id) VALUES (9, 'starwars');
      INSERT INTO rating (`value`, movie_id) VALUES (10, 'starwars');
      INSERT INTO rating (`value`, movie_id) VALUES (4, 'indianajones');
      INSERT INTO rating (`value`, movie_id) VALUES (7, 'indianajones');
      INSERT INTO rating (`value`, movie_id) VALUES (3, 'indianajones');
      INSERT INTO rating (`value`, movie_id) VALUES (9, 'indianajones');
      """.trimIndent()).executeAwait()

    getTitleByMovieId = client.preparedQuery("SELECT title FROM movie WHERE id = ?")
    insertRatingWithMovieId = client.preparedQuery("INSERT INTO rating (`value`, movie_id) VALUES (?, ?)")
    getAverageRatingByMovieId = client.preparedQuery("SELECT AVG(`value`) AS avg_rating FROM rating WHERE movie_id = ? ")

    // Build Vert.x Web router
    val router = Router.router(vertx)
    router.get("/movie/:id").coroutineHandler { ctx -> getMovie(ctx) }
    router.post("/rateMovie/:id").coroutineHandler { ctx -> rateMovie(ctx) }
    router.get("/getRating/:id").coroutineHandler { ctx -> getRating(ctx) }

    // Start the server
    vertx.createHttpServer()
        .requestHandler(router)
        .listenAwait(config.getInteger("http.port", 8080))
  }

  // Send info about a movie
  suspend fun getMovie(ctx: RoutingContext) {
    val movieId = ctx.pathParam("id")

    val rowSet = getTitleByMovieId.executeAwait(Tuple.of(movieId))
    if (rowSet.size() == 1) {
      val row = rowSet.iterator().next()
      ctx.response().end(json {
        obj("id" to movieId, "title" to row.getString("title")).encode()
      })
    } else{
      ctx.response().setStatusCode(404).end()
    }
  }

  // Rate a movie
  suspend fun rateMovie(ctx: RoutingContext) {
    val movieId = ctx.pathParam("id")
    val rating = Integer.parseInt(ctx.queryParam("getRating")[0])
    val titleRowSet = getTitleByMovieId.executeAwait(Tuple.of(movieId))
    if (titleRowSet.size() == 1) {
      insertRatingWithMovieId.executeAwait(Tuple.of(rating, movieId))
      ctx.response().setStatusCode(200).end()
    } else {
      ctx.response().setStatusCode(404).end()
    }
  }

  // Get the current rating of a movie
  suspend fun getRating(ctx: RoutingContext) {
    val movieId = ctx.pathParam("id")
    val result = getAverageRatingByMovieId.executeAwait(Tuple.of(movieId))
    val averageRating = result.iterator().next().getInteger("avg_rating")
    ctx.response().end(json {
      obj("id" to movieId, "getRating" to averageRating).encode()
    })
  }

  /**
   * An extension method for simplifying coroutines usage with Vert.x Web routers
   */
  fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
    handler { ctx ->
      launch(ctx.vertx().dispatcher()) {
        try {
          fn(ctx)
        } catch (e: Exception) {
          ctx.fail(e)
        }
      }
    }
  }
}

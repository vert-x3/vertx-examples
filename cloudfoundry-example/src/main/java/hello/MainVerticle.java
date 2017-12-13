package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {

    /**
     * Default HTTP port.
     * Can be overwritten by PORT env variable.
     */
    public static int PORT = 8080;

    private static Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    public static void main(String[] args) {
        // Vertx core
        Vertx vertx = Vertx.vertx();

        // Deploy Verticle
        vertx.deployVerticle(new MainVerticle(), res -> {
            if (!res.succeeded()) {
                logger.error("FATAL: Deploy Verticle failed!");
            }
        });
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);

        // PORT env check
        try {
            PORT = Integer.parseInt(System.getenv("PORT"));
        } catch (Exception e) {
            logger.warn("Environment variable PORT not found or not valid. Defautling to: " + PORT);
        }

        // Create HTTP server
        HttpServer httpServer = getVertx().createHttpServer();

        // Set Router
        Router mainRouter = Router.router(getVertx());
        mainRouter.route("/*")
                .handler(StaticHandler.create());
        httpServer.requestHandler(mainRouter::accept);

        // Start listening
        httpServer.listen(PORT, handler -> {
            if (handler.succeeded()) {
                logger.info("Listening on port: " + PORT);
            } else {
                logger.error("Failed to bind on port " + PORT + ". Is it being used?");
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
        logger.info("Stopped listening on port: " + PORT);
    }
}

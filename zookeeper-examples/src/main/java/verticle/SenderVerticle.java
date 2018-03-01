package verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class SenderVerticle extends AbstractVerticle {

    private HttpServer httpServer;
    private Router router;
    private EventBus eventBus;

    @Override
    public void start() throws Exception {
        assignEventBus();
        createHttpServer();
        createRouter();
        addHandlersToRouter();
        addWelcomePageRoute();
        addMessageRoute();
        configureRouterOnServer();
        listen();
    }

    private void assignEventBus() {
        eventBus = vertx.eventBus();
    }

    private void createHttpServer() {
        httpServer = vertx.createHttpServer();
    }

    private void createRouter() {
        router = Router.router(vertx);
    }

    private void addHandlersToRouter() {
        router.route().handler(BodyHandler.create());
    }

    private void addWelcomePageRoute() {
        router.route("/").handler(routingContext -> {
            HttpServerResponse httpServerResponse = routingContext.response();
            httpServerResponse.setChunked(true);
            httpServerResponse.write("Welcome to Zookeeper with clustered event bus example");
            routingContext.response().end();
        });
    }

    private void addMessageRoute() {
        router.route(HttpMethod.POST, "/user").handler(routingContext -> {
            JsonObject jsonMessage = routingContext.getBodyAsJson();
            eventBus.send("Weather", jsonMessage, messageAsyncResult -> {
                if(messageAsyncResult.succeeded()) {
                    JsonObject jsonReply = (JsonObject) messageAsyncResult.result().body();
                    System.out.println("received reply: " + jsonReply.getValue("reply"));
                    routingContext.response().end(jsonReply.toBuffer());
                }
            });
        });
    }

    private void configureRouterOnServer() {
        httpServer.requestHandler(router::accept);
    }

    private void listen() {
        httpServer.listen(8080);
    }
}

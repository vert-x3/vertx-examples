package io.vertx.example;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by rworsnop on 8/31/15.
 */
@Component
public class StaticServer {

    private Vertx vertx;

    @Resource
    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }

    @PostConstruct
    public void createServer(){
        Router router = Router.router(vertx);

        // Serve the static pages
        router.route().handler(StaticHandler.create());

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}

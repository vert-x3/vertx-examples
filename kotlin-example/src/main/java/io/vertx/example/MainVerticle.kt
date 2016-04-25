package io.vertx.example

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router

@Suppress("unused")
class MainVerticle : AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {
        val router = createRouter()

        vertx.createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(getPort(), getHost()) { result ->
                    if (result.succeeded()) {
                        startFuture?.complete()
                    } else {
                        startFuture?.fail(result.cause())
                    }
                }
    }

    private fun createRouter() = Router.router(vertx).apply {
        get("/").handler { req ->
            req.response().end("Hello World!")
        }
        get("/test").handler { req ->
            req.response().end("Test")
        }
    }

    private fun getPort() = config().getInteger("http.port", 8080)

    private fun getHost() = config().getString("http.address", "0.0.0.0")
}
package org.digieng.vertx.kotlintest

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.http.HttpServerOptions

/**
 * Basic HTTP server used for testing.
 * @author Nick Apperley
 */
class Server : AbstractVerticle() {
    override fun start() {
        println("Starting server...")
        val serverOptions = HttpServerOptions(host = "localhost", port = 8080, ssl = false)
        val router = Router.router(vertx)

        setupRoutes(router)
        vertx.createHttpServer(serverOptions).requestHandler(router).listen()
        println("Server running on http://${serverOptions.host}:${serverOptions.port}")
    }

    private fun setupRoutes(router: Router) {
        router.get("/status").handler(statusRouteHandler())
        router.delete("/status").handler(statusRouteHandler())
        router.put("/status").handler(statusRouteHandler())
        router.post("/status").handler(statusRouteHandler())
    }

    private fun statusRouteHandler() = { ctx: RoutingContext ->
        val httpOk = 200

        with(ctx.response()) {
            statusCode = httpOk
            end()
        }
    }

    override fun stop() {
        println("Stopping server...")
    }
}

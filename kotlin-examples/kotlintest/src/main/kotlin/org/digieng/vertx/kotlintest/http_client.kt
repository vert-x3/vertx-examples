package org.digieng.vertx.kotlintest

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.kotlin.ext.web.client.WebClientOptions
import io.vertx.ext.web.client.WebClient


// Contains HTTP client functionality to use for testing.
// Author - Nick Apperley.

object HttpClient {
    var host = "localhost"
    var port = 8080
    var delay = 1000L

    fun useClient(vertx: Vertx, block: WebClient?.() -> Unit) {
        val client = WebClient.create(vertx, WebClientOptions(defaultPort = port, defaultHost = host))

        block(client)
        client.close()
    }
}

infix fun WebClient?.httpGetStatus(path: String): Int {
    var status = 0

    if (this != null) {
        get(path).send { ar ->
            if (ar.succeeded()) status = ar.result().statusCode()
            else println("HTTP Client Failure: ${ar.cause()}")
        }
        Thread.sleep(HttpClient.delay)
    }
    return status
}

infix fun WebClient?.httpPostStatus(args: Pair<String, String>): Int {
    val (path, data) = args
    var status = 0

    if (this != null) {
        post(path).sendBuffer(Buffer.buffer(data)) { ar ->
            if (ar.succeeded()) status = ar.result().statusCode()
            else println("HTTP Client Failure: ${ar.cause()}")
        }
        Thread.sleep(HttpClient.delay)
    }
    return status
}

infix fun WebClient?.httpPutStatus(args: Pair<String, String>): Int {
    val (path, data) = args
    var status = 0

    if (this != null) {
        put(path).sendBuffer(Buffer.buffer(data)) { ar ->
            if (ar.succeeded()) status = ar.result().statusCode()
            else println("HTTP Client Failure: ${ar.cause()}")
        }
        Thread.sleep(HttpClient.delay)
    }
    return status
}

infix fun WebClient?.httpDeleteStatus(args: Pair<String, String>): Int {
    val (path, data) = args
    var status = 0

    if (this != null) {
        delete(path).sendBuffer(Buffer.buffer(data)) { ar ->
            if (ar.succeeded()) status = ar.result().statusCode()
            else println("HTTP Client Failure: ${ar.cause()}")
        }
        Thread.sleep(HttpClient.delay)
    }
    return status
}
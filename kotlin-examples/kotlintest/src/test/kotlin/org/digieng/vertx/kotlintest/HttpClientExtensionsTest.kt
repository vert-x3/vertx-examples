package org.digieng.vertx.kotlintest

import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.FunSpec
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod

/**
 * Contains tests for HttpClient extensions.
 * @author Nick Apperley
 */
class HttpClientExtensionsTest : FunSpec() {
    private val httpOk = 200
    private var statusCode = 0
    private var vertx: Vertx? = null

    private fun beforeTest() {
        vertx = Vertx.vertx()
        vertx!!.deployVerticle(Server())
    }

    private fun afterTest() {
        vertx?.close()
        vertx = null
    }

    init {
        testHttpGetStatus()
        testHttpDeleteStatus()
        testHttpPutStatus()
        testHttpPostStatus()
    }

    private fun updateStatusCodeByHttp(
        initialDelay: Long = 1000L,
        path: String,
        data: String = "",
        httpMethod: HttpMethod
    ) {
        if (initialDelay > 0L) Thread.sleep(initialDelay)
        HttpClient.delay = 500L
        HttpClient.useClient(vertx!!) {
            statusCode = when (httpMethod) {
                HttpMethod.POST -> this httpPostStatus (path to data)
                HttpMethod.PUT -> this httpPutStatus (path to data)
                HttpMethod.DELETE -> this httpDeleteStatus (path to data)
                else -> this httpGetStatus path
            }
        }
    }

    private fun testHttpGetStatus() = test("Function httpGetStatus returns correct status") {
        runTest(before = this::beforeTest, after = this::afterTest) {
            updateStatusCodeByHttp(path = "/status", httpMethod = HttpMethod.GET)
            println("HTTP Status: $statusCode")
            (statusCode == httpOk) shouldEqual true
        }
    }

    private fun testHttpPutStatus() = test("Function httpPutStatus returns correct status") {
        runTest(before = this::beforeTest, after = this::afterTest) {
            updateStatusCodeByHttp(path = "/status", httpMethod = HttpMethod.PUT)
            println("HTTP Status: $statusCode")
            (statusCode == httpOk) shouldEqual true
        }
    }

    private fun testHttpDeleteStatus() = test("Function httpDeleteStatus returns correct status") {
        runTest(before = this::beforeTest, after = this::afterTest) {
            updateStatusCodeByHttp(path = "/status", httpMethod = HttpMethod.DELETE)
            println("HTTP Status: $statusCode")
            (statusCode == httpOk) shouldEqual true
        }
    }

    private fun testHttpPostStatus() = test("Function httpPostStatus returns correct status") {
        runTest(before = this::beforeTest, after = this::afterTest) {
            updateStatusCodeByHttp(path = "/status", httpMethod = HttpMethod.POST)
            println("HTTP Status: $statusCode")
            (statusCode == httpOk) shouldEqual true
        }
    }
}
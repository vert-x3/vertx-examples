package org.digieng.vertx.kotlintest

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Request.Builder as RequestBuilder

// Contains HTTP client functionality to use for testing.
// Author - Nick Apperley.

object HttpClient {
    var host = "localhost"
    var port = 8080
    val client
        get() = OkHttpClient()
}

@Suppress("PrivatePropertyName")
private val PLAIN_TEXT = MediaType.parse("text/plain")

infix fun OkHttpClient.httpGetStatus(path: String): Int {
    val req = RequestBuilder().run {
        url("http://${HttpClient.host}:${HttpClient.port}$path")
        build()
    }
    val resp = newCall(req).execute()

    return resp.code()
}

infix fun OkHttpClient.httpPostStatus(args: Pair<String, String>): Int {
    val (path, data) = args
    val req = RequestBuilder().run {
        url("http://${HttpClient.host}:${HttpClient.port}$path")
        post(RequestBody.create(PLAIN_TEXT, data))
        build()
    }
    val resp = newCall(req).execute()

    return resp.code()
}

infix fun OkHttpClient.httpPutStatus(args: Pair<String, String>): Int {
    val (path, data) = args
    val req = RequestBuilder().run {
        url("http://${HttpClient.host}:${HttpClient.port}$path")
        put(RequestBody.create(PLAIN_TEXT, data))
        build()
    }
    val resp = newCall(req).execute()

    return resp.code()
}

infix fun OkHttpClient.httpDeleteStatus(args: Pair<String, String>): Int {
    val (path, data) = args
    val req = RequestBuilder().run {
        url("http://${HttpClient.host}:${HttpClient.port}$path")
        delete(RequestBody.create(PLAIN_TEXT, data))
        build()
    }
    val resp = newCall(req).execute()

    return resp.code()
}
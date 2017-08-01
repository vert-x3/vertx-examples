package io.vertx.example.webclient.oauth

import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.codec.BodyCodec

class TwitterOAuthExample : io.vertx.core.AbstractVerticle()  {
  var AUTH_URL = "https://api.twitter.com/oauth2/token"
  var TWEET_SEARCH_URL = "https://api.twitter.com/1.1/search/tweets.json"
  var B64_ENCODED_AUTH = "base64(your-consumer-key:your-consumer-secret)"
  override fun start() {

    // Create the web client.
    var client = WebClient.create(vertx)

    var queryToSearch = "vertx"

    // First we need to authenticate our call.
    var authHeader = "Basic ${B64_ENCODED_AUTH}"
    client.postAbs(AUTH_URL).as(BodyCodec.jsonObject()).addQueryParam("grant_type", "client_credentials").putHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8").putHeader("Authorization", authHeader).send({ authHandler ->
      // Authentication successful.
      if (authHandler.succeeded() && 200 == authHandler.result().statusCode()) {
        var authJson = authHandler.result().body()
        var accessToken = authJson.getString("access_token")
        var header = "Bearer ${accessToken}"
        // Making call to search tweets.
        client.getAbs(TWEET_SEARCH_URL).as(BodyCodec.jsonObject()).addQueryParam("q", queryToSearch).putHeader("Authorization", header).send({ handler ->
          if (handler.succeeded() && 200 == handler.result().statusCode()) {
            println(handler.result().body())
          } else {
            println(handler.cause().getMessage())
          }
        })
      } else {
        println(authHandler.cause().getMessage())
      }
    })
  }
}

import groovy.transform.Field
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.codec.BodyCodec
@Field def AUTH_URL = "https://api.twitter.com/oauth2/token"
@Field def TWEET_SEARCH_URL = "https://api.twitter.com/1.1/search/tweets.json"
@Field def B64_ENCODED_AUTH = "base64(your-consumer-key:your-consumer-secret)"

// Create the web client.
def client = WebClient.create(vertx)

def queryToSearch = "vertx"

// First we need to authenticate our call.
def authHeader = "Basic ${B64_ENCODED_AUTH}"
client.postAbs(AUTH_URL).as(BodyCodec.jsonObject()).addQueryParam("grant_type", "client_credentials").putHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8").putHeader("Authorization", authHeader).send({ authHandler ->
  // Authentication successful.
  if (authHandler.succeeded() && 200 == authHandler.result().statusCode()) {
    def authJson = authHandler.result().body()
    def accessToken = authJson.access_token
    def header = "Bearer ${accessToken}"
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

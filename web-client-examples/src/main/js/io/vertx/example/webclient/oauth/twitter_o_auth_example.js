var WebClient = require("vertx-web-client-js/web_client");
var BodyCodec = require("vertx-web-common-js/body_codec");
var AUTH_URL = "https://api.twitter.com/oauth2/token";
var TWEET_SEARCH_URL = "https://api.twitter.com/1.1/search/tweets.json";
var B64_ENCODED_AUTH = "base64(your-consumer-key:your-consumer-secret)";

// Create the web client.
var client = WebClient.create(vertx);

var queryToSearch = "vertx";

// First we need to authenticate our call.
var authHeader = "Basic " + B64_ENCODED_AUTH;
client.postAbs(AUTH_URL).as(BodyCodec.jsonObject()).addQueryParam("grant_type", "client_credentials").putHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8").putHeader("Authorization", authHeader).send(function (authHandler, authHandler_err) {
  // Authentication successful.
  if (authHandler_err == null && 200 === authHandler.statusCode()) {
    var authJson = authHandler.body();
    var accessToken = authJson.access_token;
    var header = "Bearer " + accessToken;
    // Making call to search tweets.
    client.getAbs(TWEET_SEARCH_URL).as(BodyCodec.jsonObject()).addQueryParam("q", queryToSearch).putHeader("Authorization", header).send(function (handler, handler_err) {
      if (handler_err == null && 200 === handler.statusCode()) {
        console.log(handler.body());
      } else {
        console.log(handler_err.getMessage());
      }
    });
  } else {
    console.log(authHandler_err.getMessage());
  }
});

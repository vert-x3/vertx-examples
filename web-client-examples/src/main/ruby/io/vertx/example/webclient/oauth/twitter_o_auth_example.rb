require 'vertx-web-client/web_client'
require 'vertx-web-common/body_codec'
@AUTH_URL = "https://api.twitter.com/oauth2/token"
@TWEET_SEARCH_URL = "https://api.twitter.com/1.1/search/tweets.json"
@B64_ENCODED_AUTH = "base64(your-consumer-key:your-consumer-secret)"

# Create the web client.
client = VertxWebClient::WebClient.create($vertx)

queryToSearch = "vertx"

# First we need to authenticate our call.
authHeader = "Basic #{@B64_ENCODED_AUTH}"
client.post_abs(@AUTH_URL).as(VertxWebCommon::BodyCodec.json_object()).add_query_param("grant_type", "client_credentials").put_header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8").put_header("Authorization", authHeader).send() { |authHandler_err,authHandler|
  # Authentication successful.
  if (authHandler_err == nil && 200 == authHandler.status_code())
    authJson = authHandler.body()
    accessToken = authJson['access_token']
    header = "Bearer #{accessToken}"
    # Making call to search tweets.
    client.get_abs(@TWEET_SEARCH_URL).as(VertxWebCommon::BodyCodec.json_object()).add_query_param("q", queryToSearch).put_header("Authorization", header).send() { |handler_err,handler|
      if (handler_err == nil && 200 == handler.status_code())
        puts handler.body()
      else
        puts handler_err.get_message()
      end
    }
  else
    puts authHandler_err.get_message()
  end
}

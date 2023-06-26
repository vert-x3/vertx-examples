package io.vertx.example.webclient.oauth;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

/*
 * @author <a href="mailto:akshay0007k@gmail.com">Akshay Kumar</a>
 */
public class TwitterOAuthExample extends AbstractVerticle {

  // consumer key and secret are provided by twitter after registering your app.
  private static final String B64_ENCODED_AUTH = "base64(your-consumer-key:your-consumer-secret)";
  private static final String AUTH_URL = "https://api.twitter.com/oauth2/token";
  private static final String TWEET_SEARCH_URL = "https://api.twitter.com/1.1/search/tweets.json";

  public static void main(String[] args) {
    Launcher.executeCommand("run", TwitterOAuthExample.class.getName());
  }

  @Override
  public void start() throws Exception {

    // Create the web client.
    WebClient client = WebClient.create(vertx);

    String queryToSearch = "vertx";

    // First we need to authenticate our call.
    String authHeader = "Basic " + B64_ENCODED_AUTH;
    client.postAbs(AUTH_URL)
      .as(BodyCodec.jsonObject())
      .addQueryParam("grant_type", "client_credentials")
      .putHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
      .putHeader("Authorization", authHeader)
      .send(authHandler -> {
        // Authentication successful.
        if (authHandler.succeeded() && 200 == authHandler.result().statusCode()) {
          JsonObject authJson = authHandler.result().body();
          String accessToken = authJson.getString("access_token");
          String header = "Bearer " + accessToken;
          // Making call to search tweets.
          client.getAbs(TWEET_SEARCH_URL)
            .as(BodyCodec.jsonObject())
            .addQueryParam("q", queryToSearch)
            .putHeader("Authorization", header)
            .send(handler -> {
              if (handler.succeeded() && 200 == handler.result().statusCode()) {
                System.out.println(handler.result().body());
              } else {
                System.out.println(handler.cause().getMessage());
              }
            });
        } else { // Authentication failed
          System.out.println(authHandler.cause().getMessage());
        }
      });
  }
}

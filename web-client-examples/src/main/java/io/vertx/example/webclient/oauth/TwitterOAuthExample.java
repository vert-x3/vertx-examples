package io.vertx.example.webclient.oauth;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

import java.util.Base64;

/*
 * @author <a href="mailto:akshay0007k@gmail.com">Akshay Kumar</a>
 */
public class TwitterOAuthExample extends AbstractVerticle {

    // Provided by twitter after registering your app.
    private static final String CONSUMER_KEY = "your-consumer-key";
    private static final String CONSUMER_SECRET = "your-consumer-secret";

    private static final String AUTH_URL = "https://api.twitter.com/oauth2/token";
    private static final String TWEET_SEARCH_URL = "https://api.twitter.com/1.1/search/tweets.json";

    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        Runner.runExample(TwitterOAuthExample.class);
    }

    @Override
    public void start() throws Exception {

        // Create the web client.
        WebClient client = WebClient.create(vertx);

        String queryToSearch = "vertx";

        // First we need to authenticate our call.
        String encodedAuth = getEncodedAuth();
        String authHeader = "Basic " + encodedAuth;
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

    private static String getEncodedAuth() {
        String key = CONSUMER_KEY + ":" + CONSUMER_SECRET;
        return Base64.getEncoder().encodeToString(key.getBytes());
    }
}

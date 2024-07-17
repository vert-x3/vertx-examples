package io.vertx.example.reactivex.web.client.zip;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.codec.BodyCodec;

/*
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public void start() throws Exception {

    // Create two requests
    WebClient client = WebClient.create(vertx);
    Single<JsonObject> request = client.get(8080, "localhost", "/")
      .as(BodyCodec.jsonObject())
      .rxSend()
      .map(HttpResponse::body);

    // Combine the responses with the zip into a single response
    request
      .zipWith(request, (b1, b2) -> new JsonObject().put("req1", b1).put("req2", b2))
      .subscribe(json -> {
        System.out.println("Got combined result " + json);
      }, Throwable::printStackTrace);
  }
}

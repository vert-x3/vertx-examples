package io.vertx.example.grpc.transcoding;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.*;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.launcher.application.VertxApplication;

public class ServerStreamingClient extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{ServerStreamingClient.class.getName()});
  }

  private HttpClientAgent client;

  @Override
  public Future<?> start() throws Exception {
    client = vertx.createHttpClient();
    return client.request(HttpMethod.GET, 8080, "localhost", "/v1/example/serverstreaming/Julien")
      .compose(request -> request.putHeader(HttpHeaders.CONTENT_TYPE, "application/json").send()
        .expecting(HttpResponseExpectation.SC_OK)
        .compose(response -> {
          Promise<Void> promise = Promise.promise();
          JsonParser.newParser(response)
            .objectValueMode()
            .handler(event -> {
              switch (event.type()) {
                case START_ARRAY:
                  System.out.println("Stream begins");
                  break;
                case VALUE:
                  System.out.println("Got item " + event.objectValue());
                  break;
                case END_ARRAY:
                  System.out.println("Stream ends");
                  break;
              }
            })
            .endHandler(promise::complete);
          return promise.future();
        })
      );
  }
}

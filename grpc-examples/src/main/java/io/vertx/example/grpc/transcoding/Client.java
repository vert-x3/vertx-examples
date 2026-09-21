package io.vertx.example.grpc.transcoding;

import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.http.*;
import io.vertx.launcher.application.VertxApplication;

public class Client extends VerticleBase {

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  private HttpClientAgent client;

  @Override
  public Future<?> start() throws Exception {
    client = vertx.createHttpClient();
    return client.request(HttpMethod.GET, 8080, "localhost", "/v1/example/Julien")
      .compose(request -> request.putHeader(HttpHeaders.CONTENT_TYPE, "application/json").send()
        .expecting(HttpResponseExpectation.SC_OK)
        .compose(HttpClientResponse::body)
      )
      .onSuccess(body -> System.out.println("Got reply " + body));
  }
}

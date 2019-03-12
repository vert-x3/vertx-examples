package io.vertx.example;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class HelloWorldVerticle extends AbstractVerticle {

  private ConfigRetriever configRetriever;
  private JsonObject config;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    retrieveConfig().setHandler(ar -> {
      if (ar.succeeded()) {
        vertx.createHttpServer()
          .requestHandler(req -> req.response().end("Hello World!"))
          .listen(config.getInteger("http.port"), config.getString("http.address", "0.0.0.0"));
        startFuture.complete();
      } else {
        startFuture.fail(ar.cause());
      }
    });
  }

  @Override
  public void stop() throws Exception {
    configRetriever.close();
    super.stop();
  }

  private Future<Void> retrieveConfig() {
    final Future<Void> future = Future.future();

    // Option for reading the Config Vars which are defined in the settings of the application on Heroku
    final ConfigStoreOptions configStoreEnvOptions = new ConfigStoreOptions()
      .setType("env")
      .setOptional(true);

    final ConfigStoreOptions configStoreSysOptions = new ConfigStoreOptions()
      .setType("sys")
      .setOptional(true);

    final ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions()
      .addStore(configStoreEnvOptions)
      .addStore(configStoreSysOptions);

    configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
    configRetriever.getConfig(ar -> {
      if (ar.succeeded()) {
        config = ar.result();
        future.complete();
      } else {
        future.fail(ar.cause());
      }
    });

    return future;
  }
}

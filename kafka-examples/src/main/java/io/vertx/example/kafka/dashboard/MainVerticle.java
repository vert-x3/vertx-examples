package io.vertx.example.kafka.dashboard;

import io.debezium.kafka.KafkaCluster;
import io.debezium.util.Testing;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  private KafkaCluster kafkaCluster;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.executeBlocking(() -> {
        // Kafka setup for the example
        File dataDir = Testing.Files.createTestingDirectory("cluster");
        dataDir.deleteOnExit();
        return new KafkaCluster()
          .usingDirectory(dataDir)
          .withPorts(2181, 9092)
          .addBrokers(1)
          .deleteDataPriorToStartup(true)
          .startup();
      })
      .compose(kafkaCluster -> {
        this.kafkaCluster = kafkaCluster;
        // Deploy the dashboard
        JsonObject consumerConfig = new JsonObject((Map) kafkaCluster.useTo()
          .getConsumerProperties("the_group", "the_client", OffsetResetStrategy.LATEST));
        return vertx.deployVerticle(
          DashboardVerticle.class.getName(),
          new DeploymentOptions().setConfig(consumerConfig)
        );
      }).compose(v -> {
        List<Future<?>> futures = new ArrayList<>();
        // Deploy the metrics collector : 3 times
        for (int i = 0; i < 3; i++) {
          JsonObject producerConfig = new JsonObject((Map) kafkaCluster.useTo()
            .getProducerProperties("the_producer-" + i));
          Future<String> future = vertx.deployVerticle(
            MetricsVerticle.class.getName(),
            new DeploymentOptions().setConfig(producerConfig)
          );
          futures.add(future);
        }
        return Future.all(futures).<Void>mapEmpty();
      }).onComplete(startPromise);
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    vertx.<Void>executeBlocking(() -> {
      kafkaCluster.shutdown();
      return null;
    }).onComplete(stopPromise);
  }
}

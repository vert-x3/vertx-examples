package io.vertx.example.kafka.dashboard;

import io.debezium.kafka.KafkaCluster;
import io.debezium.util.Testing;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.io.File;
import java.util.Map;
import java.util.Properties;

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
  public void start() throws Exception {

    // Kafka setup for the example
    File dataDir = Testing.Files.createTestingDirectory("cluster");
    dataDir.deleteOnExit();
    kafkaCluster = new KafkaCluster()
      .usingDirectory(dataDir)
      .withPorts(2181, 9092)
      .addBrokers(1)
      .deleteDataPriorToStartup(true)
      .startup();

    // Deploy the dashboard
    JsonObject consumerConfig = new JsonObject((Map) kafkaCluster.useTo()
      .getConsumerProperties("the_group", "the_client", OffsetResetStrategy.LATEST));
    vertx.deployVerticle(
      DashboardVerticle.class.getName(),
      new DeploymentOptions().setConfig(consumerConfig)
    );

    // Deploy the metrics collector : 3 times
    JsonObject producerConfig = new JsonObject((Map) kafkaCluster.useTo()
      .getProducerProperties("the_producer"));
    vertx.deployVerticle(
      MetricsVerticle.class.getName(),
      new DeploymentOptions().setConfig(producerConfig).setInstances(3)
    );
  }

  @Override
  public void stop() throws Exception {
    kafkaCluster.shutdown();
  }
}

package io.vertx.example.kafka.dashboard;

import com.sun.management.OperatingSystemMXBean;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaWriteStream;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.lang.management.ManagementFactory;
import java.util.UUID;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class MetricsVerticle extends AbstractVerticle {

  private OperatingSystemMXBean systemMBean;
  private KafkaWriteStream<String, String> producer;

  @Override
  public void start() throws Exception {
    systemMBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    // A random identifier
    String pid = UUID.randomUUID().toString();

    // Get the kafka producer config
    JsonObject config = config();

    // Create the producer
    producer = KafkaWriteStream.create(vertx, config.getMap(), String.class, String.class);

    // Publish the metircs in Kafka
    vertx.setPeriodic(1000, id -> {
      JsonObject metrics = new JsonObject();
      metrics.put("CPU", systemMBean.getProcessCpuLoad());
      metrics.put("Mem", systemMBean.getTotalPhysicalMemorySize() - systemMBean.getFreePhysicalMemorySize());
      producer.write(new ProducerRecord<>("the_topic", new JsonObject().put(pid, metrics).encode()));
    });
  }

  @Override
  public void stop() throws Exception {
    if (producer != null) {
      producer.close();
    }
  }
}

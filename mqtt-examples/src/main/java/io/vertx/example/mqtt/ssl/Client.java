package io.vertx.example.mqtt.ssl;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.example.mqtt.util.Runner;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

public class Client extends AbstractVerticle {

  private static final String MQTT_TOPIC = "/my_topic";
  private static final String MQTT_MESSAGE = "Hello Vert.x MQTT Client";
  private static final String SERVER_HOST = "0.0.0.0";
  private static final int SERVER_PORT = 8883;

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(io.vertx.example.mqtt.ssl.Client.class);
  }

  @Override
  public void start() throws Exception {
    MqttClientOptions options = new MqttClientOptions();
      options.setPort(SERVER_PORT);
      options.setHost(SERVER_HOST);
      options.setSsl(true);
      options.setTrustAll(true);

    MqttClient mqttClient = MqttClient.create(vertx, options);

    mqttClient.connect(ch -> {
      if (ch.succeeded()) {
        System.out.println("Connected to a server");

        mqttClient.publish(
          MQTT_TOPIC,
          Buffer.buffer(MQTT_MESSAGE),
          MqttQoS.AT_MOST_ONCE,
          false,
          false,
          s -> mqttClient.disconnect(d -> System.out.println("Disconnected from server")));
      } else {
        System.out.println("Failed to connect to a server");
        System.out.println(ch.cause());
      }
    });
  }

}

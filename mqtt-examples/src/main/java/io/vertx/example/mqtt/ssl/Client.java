package io.vertx.example.mqtt.ssl;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

public class Client extends AbstractVerticle {

  private static final String MQTT_TOPIC = "/my_topic";
  private static final String MQTT_MESSAGE = "Hello Vert.x MQTT Client";
  private static final String BROKER_HOST = "localhost";
  private static final int BROKER_PORT = 8883;

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Client.class.getName()});
  }

  @Override
  public void start() throws Exception {
    MqttClientOptions options = new MqttClientOptions();
      options.setSsl(true);
      options.setTrustAll(true);

    MqttClient mqttClient = MqttClient.create(vertx, options);

    mqttClient.connect(BROKER_PORT, BROKER_HOST).onComplete(ch -> {
      if (ch.succeeded()) {
        System.out.println("Connected to a server");

        mqttClient.publish(
            MQTT_TOPIC,
            Buffer.buffer(MQTT_MESSAGE),
            MqttQoS.AT_MOST_ONCE,
            false,
            false)
          .compose(s -> mqttClient.disconnect())
          .onComplete(d -> System.out.println("Disconnected from server"));
      } else {
        System.out.println("Failed to connect to a server");
        System.out.println(ch.cause());
      }
    });
  }

}

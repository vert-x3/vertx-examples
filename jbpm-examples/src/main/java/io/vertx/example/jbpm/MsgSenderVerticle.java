package io.vertx.example.jbpm;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class MsgSenderVerticle extends AbstractVerticle {

	@Override
	public void start() {
		System.out.println("Starting MsgSenderVerticle");
		EventBus eb = vertx.eventBus();
		vertx.setPeriodic(10000, v -> eb.publish("process-message", "start process!"));
	}

}

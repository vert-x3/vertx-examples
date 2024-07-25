/*
* Copyright 2018 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package io.vertx.example.proton.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.launcher.application.VertxApplication;
import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonConnection;
import org.apache.qpid.proton.amqp.messaging.AmqpValue;

public class Receiver extends AbstractVerticle {

  private String address = "examples";

  public static void main(String[] args) {
    VertxApplication.main(new String[]{Receiver.class.getName()});
  }

  @Override
  public void start() throws Exception {
    ProtonClient client = ProtonClient.create(vertx);

    client.connect("localhost", 5672, res -> {
      if(!res.succeeded()) {
        System.out.println("Connect failed: " + res.cause());
        return;
      }

      ProtonConnection connection = res.result();
      connection.open();

      connection.createReceiver(address).handler((delivery, msg) -> {
        String content = (String) ((AmqpValue)  msg.getBody()).getValue();
        System.out.println("Received message with content: " + content);

        // By default, receivers automatically accept (and settle) the delivery
        // when the handler returns, if no other disposition has been applied.
        // To change this and always manage dispositions yourself, use the
        // setAutoAccept method on the receiver.
      }).open();
    });
  }
}

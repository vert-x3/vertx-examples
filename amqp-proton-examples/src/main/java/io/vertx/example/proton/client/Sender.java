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

import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonSender;

import static io.vertx.proton.ProtonHelper.message;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.qpid.proton.message.Message;

import io.vertx.core.AbstractVerticle;
import io.vertx.example.util.Runner;

public class Sender extends AbstractVerticle {

  private String address = "examples";
  private AtomicInteger sent = new AtomicInteger();

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Sender.class);
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

      ProtonSender sender = connection.createSender(address);

      // Can optionally add an openHandler and/or sendQueueDrainHandler
      // to await remote sender open completing and credit to send being
      // granted. Here we will just schedule sends to happen if there
      // is credit at the time.
      sender.open();

      // Schedule sending of a message every second
      System.out.println("Sender created, scheduling sends.");

      vertx.setPeriodic(1000, x -> {
        if(!sender.sendQueueFull()) {
          final int msgNum = sent.incrementAndGet();
          Message message = message("Hello " + msgNum);

          sender.send(message, delivery -> {
            System.out.println(String.format("Message " + msgNum + " was received by the server: remote state=%s", delivery.getRemoteState()));
          });

          System.out.println("Sent message: " + msgNum);
        } else {
          System.out.println("No credit to send, waiting.");
        }
      });
    });
  }
}

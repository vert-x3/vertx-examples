/*
* Copyright 2019 the original author or authors.
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

import static io.vertx.proton.ProtonHelper.message;

import java.time.Instant;

import org.apache.qpid.proton.message.Message;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.example.util.Runner;
import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonSender;

public class ReconnectSender extends AbstractVerticle {

  private static final int MSG_COUNT = 20000;
  private static final String ADDRESS = "examples";

  private ConnectionControl connectionControl = new ConnectionControl("localhost:5672", "localhost:15672");

  private int sent;
  private int confirmed;
  private ProtonConnection conn;

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(ReconnectSender.class);
  }

  @Override
  public void start() throws Exception {
    ProtonClient client = ProtonClient.create(vertx);

    setupConnection(client);
  }

  private void setupConnection(ProtonClient client) {
    String peer = connectionControl.nextPeer();

    System.out.println("Attempting to connect to peer: " + peer);

    String[] peerDetails = peer.split(":");
    client.connect(peerDetails[0], Integer.parseInt(peerDetails[1]), res -> {
      if(!res.succeeded()) {
        System.out.println("Connect failed: " + res.cause());

        handleConnectionFailure(client, null, false);
        return;
      }

      ProtonConnection connection = res.result();
      conn = connection;

      connection.openHandler(x -> {
        connectionControl.connected();
        setupSender(connection);
      })
      .closeHandler(x -> {
        handleConnectionFailure(client, connection, true);
      })
      .disconnectHandler(x -> {
        handleConnectionFailure(client, connection, false);
      })
      .open();
    });
  }

  private void handleConnectionFailure(ProtonClient client, ProtonConnection oldConnection, boolean remoteClose) {
    try {
      conn = null;

      if (sent != confirmed) {
        System.out.println("Resetting sent count to confirmed count: " + confirmed);
        sent = confirmed;
      }

      if (oldConnection != null) {
        oldConnection.closeHandler(null);
        oldConnection.disconnectHandler(null);

        if (remoteClose) {
          oldConnection.close();
          oldConnection.disconnect();
        }
      }
    } finally {
      if (connectionControl.shouldReconnect()) {
        connectionControl.scheduleReconnect(client);
      }
    }
  }

  private void setupSender(ProtonConnection connection) {
    ProtonSender sender = connection.createSender(ADDRESS);

    sender.sendQueueDrainHandler(x -> {
      while(!sender.sendQueueFull() && sent < MSG_COUNT) {
        sendMessage(sender);
      }
    })
    .open();
  }

  private void sendMessage(ProtonSender sender) {
    int msgNum = sent + 1;
    System.out.println("Sending message: " + msgNum);

    Message message = message("Hello " + msgNum);

    sender.send(message, delivery -> {
      System.out.println(String.format("Message " + msgNum + " was received by the server: remote state=%s", delivery.getRemoteState()));
      confirmed++; // Just assuming it was accepted

      if(confirmed == MSG_COUNT) {
        shutdown();
      }
    });

    sent++;
  }

  private void shutdown() {
    ProtonConnection connection = conn;
    conn = null;

    if(connection != null) {
      connection.disconnectHandler(null);
      connection.closeHandler(x -> {
        try {
          connection.disconnect();
        } finally {
          vertx.close();
        }
      });

      connection.close();
    } else {
      vertx.close();
    }
  }

  private class ConnectionControl {
    private final int MAX_DELAY = 30_000;//ms
    private final int STARTING_DELAY = 10;//ms, for 2nd overall attempt onward.
    private final int BACKOFF = 2;

    private final String[] peers;
    private int peerIndex = 0;
    private int currentDelay = 0;
    private int nextDelay = 0;

    public ConnectionControl(String... peers) {
      this.peers = peers;
    }

    public void connected() {
      peerIndex = 0;
      currentDelay = 0;
      nextDelay = 0;
    }

    public String nextPeer() {
      String peer = peers[peerIndex++];
      if(peerIndex == peers.length) {
        // Tried all peers, restart next time round, after a delay.
        peerIndex = 0;

        if(nextDelay == 0) {
          nextDelay = STARTING_DELAY;
        } else {
          nextDelay = Math.min(nextDelay * BACKOFF, MAX_DELAY);
        }

        currentDelay = nextDelay;
      } else {
        currentDelay = 0;
      }

      return peer;
    }

    boolean shouldReconnect() {
      // For this example we just continue if yet to hit MSG_COUNT.
      // Could use any metric here, e.g a retry limit etc.
      return confirmed < ReconnectSender.MSG_COUNT;
    }

    void scheduleReconnect(ProtonClient client) {
      if (currentDelay <= 0) {
        Vertx.currentContext().runOnContext(x -> {
          setupConnection(client);
        });
      } else {
        System.out.println("# Scheduling connect attempt in " + currentDelay + "ms at " + Instant.now().plusMillis(currentDelay));
        vertx.setTimer(currentDelay, x -> {
          setupConnection(client);
        });
      }
    }
  }
}

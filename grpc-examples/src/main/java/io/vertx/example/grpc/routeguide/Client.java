package io.vertx.example.grpc.routeguide;

import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.examples.routeguide.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.grpc.util.Runner;
import io.vertx.grpc.VertxChannelBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  private Random random = new Random();
  private VertxRouteGuideGrpc.RouteGuideVertxStub stub;

  @Override
  public void start() throws Exception {
    ManagedChannel channel = VertxChannelBuilder
      .forAddress(vertx, "localhost", 8080)
      .usePlaintext()
      .build();

    stub = VertxRouteGuideGrpc.newVertxStub(channel);

    List<Feature> features = Util.parseFeatures(Util.getDefaultFeaturesFile());

    // Looking for a valid feature
    getFeature(409146138, -746188906);

    // Feature missing.
    getFeature(0, 0);

    // Looking for features between 40, -75 and 42, -73.
    listFeatures(400000000, -750000000, 420000000, -730000000);

    // Record a few randomly selected points from the features file.
    recordRoute(features, 10);

    routeChat();
  }

  /**
   * Blocking unary call example.  Calls getFeature and prints the response.
   */
  public void getFeature(int lat, int lon) {
    System.out.println("*** GetFeature: lat=" + lat + " lon=" + lon);

    Point request = Point.newBuilder().setLatitude(lat).setLongitude(lon).build();

    stub.getFeature(request).onComplete(ar -> {
      if (ar.succeeded()) {
        Feature feature = ar.result();
        if (Util.exists(feature)) {
          System.out.println("Found feature called " + feature.getName() +
            " at " + Util.getLatitude(feature.getLocation()) + ", " + Util.getLongitude(feature.getLocation()));
        } else {
          System.out.println("Found no feature at " + Util.getLatitude(feature.getLocation()) + ", " +
            Util.getLongitude(feature.getLocation()));
        }
      }
    });
  }

  /**
   * Blocking server-streaming example. Calls listFeatures with a rectangle of interest. Prints each
   * response feature as it arrives.
   */
  public void listFeatures(int lowLat, int lowLon, int hiLat, int hiLon) {
    System.out.println("*** ListFeatures: lowLat=" + lowLat + " lowLon=" + lowLon + " hiLat=" + hiLat + " hiLon=" + hiLon);

    Rectangle request =
      Rectangle.newBuilder()
        .setLo(Point.newBuilder().setLatitude(lowLat).setLongitude(lowLon).build())
        .setHi(Point.newBuilder().setLatitude(hiLat).setLongitude(hiLon).build()).build();

    ReadStream<Feature> response = stub.listFeatures(request);
    List<Feature> features = Collections.synchronizedList(new ArrayList<>());
    response.handler(feature -> {
      System.out.println("Result #" + features.size() + ": " + feature);
      features.add(feature);
    });

    // Neede for now as it triggers an NPE if not set
    response.endHandler(v -> {
/*
Feb 14, 2017 11:08:53 PM io.grpc.internal.SerializingExecutor$TaskRunner run
SEVERE: Exception while executing runnable io.grpc.internal.ClientCallImpl$ClientStreamListenerImpl$1StreamClosed@6668e779
java.lang.NullPointerException
	at io.vertx.grpc.impl.GrpcReadStreamImpl$1.onCompleted(GrpcReadStreamImpl.java:69)
	at io.grpc.stub.ClientCalls$StreamObserverToCallListenerAdapter.onClose(ClientCalls.java:390)
	at io.grpc.internal.ClientCallImpl.closeObserver(ClientCallImpl.java:422)
	*/
    });
  }

  class RouteSender {

    List<Feature> features;
    WriteStream<Point> writeStream;

    RouteSender(List<Feature> features, WriteStream<Point> writeStream) {
      this.features = features;
      this.writeStream = writeStream;
    }

    void send(int numPoints) {
      int index = random.nextInt(features.size());
      Point point = features.get(index).getLocation();
      System.out.println("Visiting point " + Util.getLatitude(point) + ", " + Util.getLongitude(point));
      writeStream.write(point);
      if (numPoints > 0) {
        vertx.setTimer(random.nextInt(1000) + 500, id -> {
          send(numPoints - 1);
        });
      } else {
        writeStream.end();
      }
    }
  }

  /**
   * Async client-streaming example. Sends {@code numPoints} randomly chosen points from {@code
   * features} with a variable delay in between. Prints the statistics when they are sent from the
   * server.
   */
  public void recordRoute(List<Feature> features, int numPoints) {
    System.out.println("*** RecordRoute");

    stub.recordRoute(writeStream -> {

      RouteSender sender = new RouteSender(features, writeStream);

      // Send numPoints points randomly selected from the features list.
      sender.send(numPoints);
    }).onComplete(ar -> {
      if (ar.succeeded()) {
        RouteSummary summary = ar.result();
        System.out.println("Finished trip with " + summary.getPointCount() + " points. Passed " + summary.getFeatureCount()
          + " features.Travelled " + summary.getDistance() + " meters. It took " + summary.getElapsedTime() + " seconds.");
        System.out.println("Finished RecordRoute");
      } else {
        System.out.println("RecordRoute Failed: " + Status.fromThrowable(ar.cause()));
      }
    });
  }

  /**
   * Bi-directional example, which can only be asynchronous. Send some chat messages, and print any
   * chat messages that are sent from the server.
   */
  public void routeChat() {
    System.out.println("*** RouteChat");

    ReadStream<RouteNote> readStream = stub.routeChat(writeStream -> {
      RouteNote[] requests =
        {newNote("First message", 0, 0), newNote("Second message", 0, 1),
          newNote("Third message", 1, 0), newNote("Fourth message", 1, 1)};

      for (RouteNote request : requests) {
        System.out.println("Sending message \"" + request.getMessage() + "\" at " + request.getLocation()
          .getLatitude() + ", " + request.getLocation().getLongitude());
        writeStream.write(request);
      }
      writeStream.end();
    });

    readStream.handler(note -> {
      System.out.println("Got message \"" + note.getMessage() + "\" at " + note.getLocation().getLatitude() +
        ", " + note.getLocation().getLongitude());
    });

    readStream.exceptionHandler(err -> {
      System.out.println("RouteChat Failed: " + Status.fromThrowable(err));
    });

    readStream.endHandler(v -> {
      System.out.println("Finished RouteChat");
    });
  }

  private RouteNote newNote(String message, int lat, int lon) {
    return RouteNote.newBuilder().setMessage(message)
      .setLocation(Point.newBuilder().setLatitude(lat).setLongitude(lon).build()).build();
  }
}

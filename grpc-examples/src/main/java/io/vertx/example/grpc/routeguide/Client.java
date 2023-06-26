package io.vertx.example.grpc.routeguide;

import io.grpc.Status;
import io.grpc.examples.routeguide.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.streams.WriteStream;
import io.vertx.grpc.client.GrpcClient;
import io.vertx.grpc.common.GrpcReadStream;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Client extends AbstractVerticle {

  public static void main(String[] args) {
    Launcher.executeCommand("run", Client.class.getName());
  }

  private Random random = new Random();
  private GrpcClient client;

  @Override
  public void start() throws Exception {
    // Create the channel
    client = GrpcClient.client(vertx);

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

    Point msg = Point.newBuilder().setLatitude(lat).setLongitude(lon).build();

    Future<Feature> fut = client.request(SocketAddress.inetSocketAddress(8080, "localhost"), RouteGuideGrpc.getGetFeatureMethod())
      .compose(request -> {
        request.write(msg);
        return request.response().compose(GrpcReadStream::last);
      });

    fut.onSuccess(feature -> {
        if (Util.exists(feature)) {
          System.out.println("Found feature called " + feature.getName() +
            " at " + Util.getLatitude(feature.getLocation()) + ", " + Util.getLongitude(feature.getLocation()));
        } else {
          System.out.println("Found no feature at " + Util.getLatitude(feature.getLocation()) + ", " +
            Util.getLongitude(feature.getLocation()));
        }
      });
  }

  /**
   * Blocking server-streaming example. Calls listFeatures with a rectangle of interest. Prints each
   * response feature as it arrives.
   */
  public void listFeatures(int lowLat, int lowLon, int hiLat, int hiLon) {
    System.out.println("*** ListFeatures: lowLat=" + lowLat + " lowLon=" + lowLon + " hiLat=" + hiLat + " hiLon=" + hiLon);

    Rectangle msg =
      Rectangle.newBuilder()
        .setLo(Point.newBuilder().setLatitude(lowLat).setLongitude(lowLon).build())
        .setHi(Point.newBuilder().setLatitude(hiLat).setLongitude(hiLon).build()).build();

    // We can buffer all features as the list is small
    Future<List<Feature>> fut = client.request(SocketAddress.inetSocketAddress(8080, "localhost"), RouteGuideGrpc.getListFeaturesMethod())
      .compose(request -> {
        request.write(msg);
        return request.response()
          .compose(response -> response.collecting(Collectors.toList()));
      });

    fut.onSuccess(features -> {
      features.forEach(feature -> {
        System.out.println("Result #" + features.size() + ": " + feature);
      });
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

    Future<RouteSummary> fut = client.request(SocketAddress.inetSocketAddress(8080, "localhost"), RouteGuideGrpc.getRecordRouteMethod())
      .compose(request -> {
        RouteSender sender = new RouteSender(features, request);
        sender.send(numPoints);
        return request.response()
          .compose(GrpcReadStream::last);
      });

    fut
      .onSuccess(summary -> {
      System.out.println("Finished trip with " + summary.getPointCount() + " points. Passed " + summary.getFeatureCount()
        + " features.Travelled " + summary.getDistance() + " meters. It took " + summary.getElapsedTime() + " seconds.");
      System.out.println("Finished RecordRoute");
    })
      .onFailure(cause -> System.out.println("RecordRoute Failed: " + Status.fromThrowable(cause)));
  }

  /**
   * Bi-directional example, which can only be asynchronous. Send some chat messages, and print any
   * chat messages that are sent from the server.
   */
  public void routeChat() {
    System.out.println("*** RouteChat");

    Future<Void> fut = client.request(SocketAddress.inetSocketAddress(8080, "localhost"), RouteGuideGrpc.getRouteChatMethod())
      .compose(request -> {
        RouteNote[] msgs =
          {newNote("First message", 0, 0), newNote("Second message", 0, 1),
            newNote("Third message", 1, 0), newNote("Fourth message", 1, 1)};

        for (RouteNote msg : msgs) {
          System.out.println("Sending message \"" + msg.getMessage() + "\" at " + msg.getLocation()
            .getLatitude() + ", " + msg.getLocation().getLongitude());
          request.write(msg);
        }
        request.end();
        return request.response().compose(response -> {
          response.handler(note -> {
            System.out.println("Got message \"" + note.getMessage() + "\" at " + note.getLocation().getLatitude() +
              ", " + note.getLocation().getLongitude());
          });
          return response.end();
        });
      });

    fut
      .onSuccess(v -> System.out.println("Finished RouteChat"))
      .onFailure(cause -> System.out.println("RouteChat Failed: " + Status.fromThrowable(cause)));
  }

  private RouteNote newNote(String message, int lat, int lon) {
    return RouteNote.newBuilder().setMessage(message)
      .setLocation(Point.newBuilder().setLatitude(lat).setLongitude(lon).build()).build();
  }
}

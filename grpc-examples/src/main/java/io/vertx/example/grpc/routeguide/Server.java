package io.vertx.example.grpc.routeguide;

import io.grpc.examples.routeguide.Feature;
import io.grpc.examples.routeguide.Point;
import io.grpc.examples.routeguide.Rectangle;
import io.grpc.examples.routeguide.RouteGuideGrpc;
import io.grpc.examples.routeguide.RouteNote;
import io.grpc.examples.routeguide.RouteSummary;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.example.grpc.util.Runner;
import io.vertx.grpc.GrpcBidiExchange;
import io.vertx.grpc.GrpcReadStream;
import io.vertx.grpc.GrpcWriteStream;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Server extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  private List<Feature> features;
  private final Map<Point, List<RouteNote>> routeNotes = new HashMap<>();

  @Override
  public void start() throws Exception {

    URL featureFile = Util.getDefaultFeaturesFile();
    features = Util.parseFeatures(featureFile);

    VertxServer server = VertxServerBuilder.forAddress(vertx, "localhost", 8080).addService(new RouteGuideGrpc.RouteGuideVertxImplBase() {

      @Override
      public void getFeature(Point request, Future<Feature> response) {
        response.complete(checkFeature(request));
      }

      @Override
      public void listFeatures(Rectangle request, GrpcWriteStream<Feature> response) {
        int left = Math.min(request.getLo().getLongitude(), request.getHi().getLongitude());
        int right = Math.max(request.getLo().getLongitude(), request.getHi().getLongitude());
        int top = Math.max(request.getLo().getLatitude(), request.getHi().getLatitude());
        int bottom = Math.min(request.getLo().getLatitude(), request.getHi().getLatitude());

        for (Feature feature : features) {
          if (!Util.exists(feature)) {
            continue;
          }

          int lat = feature.getLocation().getLatitude();
          int lon = feature.getLocation().getLongitude();
          if (lon >= left && lon <= right && lat >= bottom && lat <= top) {
            response.write(feature);
          }
        }
        response.end();
      }

      @Override
      public void recordRoute(GrpcReadStream<Point> request, Future<RouteSummary> response) {

        request.exceptionHandler(err -> {
          System.out.println("recordRoute cancelled");
        });

        RouteRecorder recorder = new RouteRecorder();

        request.handler(recorder::append);

        request.endHandler(v -> {
          response.complete(recorder.build());
        });
      }

      @Override
      public void routeChat(GrpcBidiExchange<RouteNote, RouteNote> exchange) {

        exchange.handler(note -> {
          List<RouteNote> notes = getOrCreateNotes(note.getLocation());

          // Respond with all previous notes at this location.
          for (RouteNote prevNote : notes.toArray(new RouteNote[0])) {
            exchange.write(prevNote);
          }

          // Now add the new note to the list
          notes.add(note);
        });

        exchange.exceptionHandler(err -> {
          System.out.println("routeChat cancelled");
        });

        exchange.endHandler(v -> exchange.end());
      }
    }).build();
    server.start(ar -> {
      if (ar.succeeded()) {
        System.out.println("gRPC service started");
      } else {
        System.out.println("Could not start server " + ar.cause().getMessage());
      }
    });
  }

  class RouteRecorder {

    int pointCount;
    int featureCount;
    int distance;
    Point previous;
    final long startTime = System.nanoTime();

    void append(Point point) {
      pointCount++;
      if (Util.exists(checkFeature(point))) {
        featureCount++;
      }
      // For each point after the first, add the incremental distance from the previous point to
      // the total distance value.
      if (previous != null) {
        distance += calcDistance(previous, point);
      }
      previous = point;
    }

    RouteSummary build() {
      long seconds = NANOSECONDS.toSeconds(System.nanoTime() - startTime);
      return RouteSummary.newBuilder().setPointCount(pointCount)
        .setFeatureCount(featureCount).setDistance(distance)
        .setElapsedTime((int) seconds).build();
    }
  }

  /**
   * Get the notes list for the given location. If missing, create it.
   */
  private List<RouteNote> getOrCreateNotes(Point location) {
    List<RouteNote> notes = Collections.synchronizedList(new ArrayList<RouteNote>());
    List<RouteNote> prevNotes = routeNotes.putIfAbsent(location, notes);
    return prevNotes != null ? prevNotes : notes;
  }

  /**
   * Gets the feature at the given point.
   *
   * @param location the location to check.
   * @return The feature object at the point. Note that an empty name indicates no feature.
   */
  private Feature checkFeature(Point location) {
    for (Feature feature : features) {
      if (feature.getLocation().getLatitude() == location.getLatitude()
        && feature.getLocation().getLongitude() == location.getLongitude()) {
        return feature;
      }
    }

    // No feature was found, return an unnamed feature.
    return Feature.newBuilder().setName("").setLocation(location).build();
  }

  /**
   * Calculate the distance between two points using the "haversine" formula.
   * This code was taken from http://www.movable-type.co.uk/scripts/latlong.html.
   *
   * @param start The starting point
   * @param end The end point
   * @return The distance between the points in meters
   */
  private static int calcDistance(Point start, Point end) {
    double lat1 = Util.getLatitude(start);
    double lat2 = Util.getLatitude(end);
    double lon1 = Util.getLongitude(start);
    double lon2 = Util.getLongitude(end);
    int r = 6371000; // meters
    double phi1 = Math.toRadians(lat1);
    double phi2 = Math.toRadians(lat2);
    double deltaPhi = Math.toRadians(lat2 - lat1);
    double deltaLambda = Math.toRadians(lon2 - lon1);

    double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
      + Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return (int) (r * c);
  }
}

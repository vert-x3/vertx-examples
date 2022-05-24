package io.vertx.example.grpc.routeguide;

import io.grpc.examples.routeguide.Feature;
import io.grpc.examples.routeguide.Point;
import io.grpc.examples.routeguide.Rectangle;
import io.grpc.examples.routeguide.RouteNote;
import io.grpc.examples.routeguide.RouteSummary;
import io.grpc.examples.routeguide.VertxRouteGuideGrpc;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.example.util.Runner;
import io.vertx.grpc.server.GrpcServer;
import io.vertx.grpc.server.GrpcServiceBridge;

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
public class ServerWithStub extends AbstractVerticle {

  public static void main(String[] args) {
    Runner.runExample(ServerWithStub.class);
  }

  private List<Feature> features;
  private final Map<Point, List<RouteNote>> routeNotes = new HashMap<>();

  @Override
  public void start() throws Exception {

    URL featureFile = Util.getDefaultFeaturesFile();
    features = Util.parseFeatures(featureFile);

    VertxRouteGuideGrpc.RouteGuideVertxImplBase service = new VertxRouteGuideGrpc.RouteGuideVertxImplBase() {

      @Override
      public Future<Feature> getFeature(Point request) {
        return Future.succeededFuture(checkFeature(request));
      }

      @Override
      public void listFeatures(Rectangle request, WriteStream<Feature> response) {
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
      public Future<RouteSummary> recordRoute(ReadStream<Point> request) {
        Promise<RouteSummary> response = Promise.promise();

        request.exceptionHandler(err -> {
          System.out.println("recordRoute cancelled");
          response.fail(err);
        });

        RouteRecorder recorder = new RouteRecorder();

        request.handler(recorder::append);

        request.endHandler(v -> {
          response.complete(recorder.build());
        });

        return response.future();
      }

      @Override
      public void routeChat(ReadStream<RouteNote> request, WriteStream<RouteNote> response) {
        request.handler(note -> {
          List<RouteNote> notes = getOrCreateNotes(note.getLocation());

          // Respond with all previous notes at this location.
          for (RouteNote prevNote : notes.toArray(new RouteNote[0])) {
            response.write(prevNote);
          }

          // Now add the new note to the list
          notes.add(note);
        });

        request.exceptionHandler(err -> {
          System.out.println("routeChat cancelled");
          response.end();
        });

        request.endHandler(v -> response.end());
      }
    };

    // Create the server
    GrpcServer rpcServer = GrpcServer.server(vertx);
    GrpcServiceBridge
      .bridge(service)
      .bind(rpcServer);

    // start the server
    vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
      .onFailure(cause -> {
        cause.printStackTrace();
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
    List<RouteNote> notes = Collections.synchronizedList(new ArrayList<>());
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

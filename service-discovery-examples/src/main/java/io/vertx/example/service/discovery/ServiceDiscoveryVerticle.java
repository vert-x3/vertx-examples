package io.vertx.example.service.discovery;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class ServiceDiscoveryVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(ServiceDiscoveryVerticle.class.getName());
  }

  @Override
  public void start() {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx,
                          new ServiceDiscoveryOptions()
                            .setAnnounceAddress("service-announce")
                            .setName("my-name"));

    //create a new custom record
    Record record1 = new Record()
      .setType("eventbus-service-proxy")
      .setLocation(new JsonObject().put("endpoint", "the-service-address"))
      .setName("my-service")
      .setMetadata(new JsonObject().put("some-label", "some-value"));

    //publish "my-service" service
    discovery.publish(record1, ar -> {
      if (ar.succeeded()) {
        System.out.println("\"" + record1.getName() + "\" successfully published!");
        Record publishedRecord = ar.result();
      } else {
        // publication failed
      }
    });

     // create a record from type
    Record record2 = HttpEndpoint.createRecord("some-rest-api", "localhost", 8080, "/api");

    //publish the service
    discovery.publish(record2, ar -> {
      if (ar.succeeded()) {
        System.out.println("\"" + record2.getName() + "\" successfully published!");
        Record publishedRecord = ar.result();
      } else {
        // publication failed
      }
    });

    //unpublish "my-service"
    discovery.unpublish(record1.getRegistration(), ar -> {
      if (ar.succeeded()) {
        System.out.println("\"" + record1.getName() + "\" successfully unpublished");
      } else {
        // cannot un-publish the service, may have already been removed, or the record is not published
      }
    });

    //consuming a service
    discovery.getRecord(r -> r.getName().equals(record2.getName()), ar -> {
      if (ar.succeeded()) {
        if (ar.result() != null) {
          // Retrieve the service reference
          ServiceReference reference = discovery.getReference(ar.result());
          // Retrieve the service object
          HttpClient client = reference.get();
          System.out.println("Consuming \"" + record2.getName() + "\"");

          client.get("/api", ar2 -> {
            //release the service
            reference.release();
          });
        }
      }

    });

    discovery.close();
  }
}

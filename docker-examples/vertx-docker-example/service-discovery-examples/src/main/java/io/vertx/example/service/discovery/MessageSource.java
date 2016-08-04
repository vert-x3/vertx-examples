import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.types.MessageSource;

public class MessageSource extends AbstractVerticle
{

    @Override
    public void run()
    {
        ServiceDiscovery discovery = new ServiceDiscovery();
        Record record = MessageSource.createRecord(
                "some-message-source-service", // The service name
                "some-address" // The event bus address
        );

        discovery.publish(record, ar -> {
            // ...
        });

        record = MessageSource.createRecord(
                "some-other-message-source-service", // The service name
                "some-address", // The event bus address
                "examples.MyData" // The payload type
        );
    }


}

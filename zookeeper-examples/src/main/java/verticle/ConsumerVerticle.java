package verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

public class ConsumerVerticle extends AbstractVerticle {

    private String verticleAddress = "Weather";
    private EventBus eventBus;

    @Override
    public void start() throws Exception {
        assignEventBus();
        registerHandler();
    }

    private void assignEventBus() {
        eventBus = vertx.eventBus();
    }

    private void registerHandler() {
        MessageConsumer<JsonObject> messageConsumer = eventBus.consumer(verticleAddress);
        messageConsumer.handler(message -> {
            JsonObject jsonMessage = message.body();
            System.out.println(jsonMessage.getValue("message_from_HTTP_request"));
            WebClient webClient = WebClient.create(vertx);
            webClient.get(443, "jsonplaceholder.typicode.com", "/posts/1").ssl(true).as(BodyCodec.jsonObject())
                    .send(httpResponseAsyncResult -> {
                        if(httpResponseAsyncResult.succeeded()) {
                            JsonObject jsonReply = httpResponseAsyncResult.result().body();
                            jsonReply.put("reply", "how interesting!");
                            message.reply(jsonReply);
                        }
                    });
        });
    }
}

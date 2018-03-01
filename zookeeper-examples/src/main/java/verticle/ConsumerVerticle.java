package verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

public class ConsumerVerticle extends AbstractVerticle {

    private String verticleAddress = "Consumer";
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
            System.out.println(jsonMessage.getValue("message_from_sender_verticle"));
            JsonObject jsonReply = new JsonObject().put("reply", "how interesting!");
            message.reply(jsonReply);
        });
    }
}

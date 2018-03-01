package verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class SenderVerticle extends AbstractVerticle {

    private EventBus eventBus;

    @Override
    public void start() throws Exception {
        assignEventBus();
        sendMessage();
    }

    private void assignEventBus() {
        eventBus = vertx.eventBus();
    }

    private void sendMessage() {
        JsonObject jsonMessage = new JsonObject().put("message_from_sender_verticle", "hello consumer");
        eventBus.send("Consumer", jsonMessage, messageAsyncResult -> {
            if(messageAsyncResult.succeeded()) {
                JsonObject jsonReply = (JsonObject) messageAsyncResult.result().body();
                System.out.println("received reply: " + jsonReply.getValue("reply"));
            }
        });
    }
}

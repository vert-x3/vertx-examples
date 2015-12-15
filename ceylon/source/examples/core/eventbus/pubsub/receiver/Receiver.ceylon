import io.vertx.ceylon.core {
  Verticle
}
import io.vertx.ceylon.core.eventbus {
  Message
}

shared class Receiver() extends Verticle() {
  
  shared actual void start() {
    value eb = vertx.eventBus();
    
    eb.consumer("ping-address", (Message<String> message ) {

      assert(exists body = message.body());
      print("Received message: ``body``");
      
      // Now send back reply
      message.reply("pong!");
    });
    
    print("Receiver ready!");
  }
}
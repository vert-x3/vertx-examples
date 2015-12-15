import io.vertx.ceylon.core {
  Verticle
}
import io.vertx.ceylon.core.eventbus {
  Message
}
shared class Sender() extends Verticle() {
  
  shared actual void start() {
    
    value eb = vertx.eventBus();
    
    vertx.setPeriodic(1000, (Integer id) => eb.send("ping-address", "ping!", void (Message<String>|Throwable reply) {
      switch(reply)
      case (is Message<String>) {
        assert(exists body = reply.body());
        print("Received reply ``body``");
      }
      else {
        print("No reply");
      }
    }));
  }
}
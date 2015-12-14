import java.lang { System }
import examples.utils {
  runModuleVerticle
}

"Run the module `examples.web.staticsite`."
shared void run() {

  // We set this property to prevent Vert.x caching files loaded from the classpath on disk
  // This means if you edit the static files in your IDE then the next time they are served the new ones will
  // be served without you having to restart the main()
  // This is only useful for development - do not use this in a production server
  System.setProperty("vertx.disableFileCaching", "true");
  
  runModuleVerticle(`module`);
    
}
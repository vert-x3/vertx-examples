import io.vertx.ceylon.web { ... }
import io.vertx.ceylon.web.handler { ... }
import io.vertx.ceylon.web.sstore { localSessionStore }
import io.vertx.ceylon.core { Verticle }
import io.vertx.ceylon.auth.shiro { shiroAuth, properties,
  ShiroAuthOptions }
import ceylon.json { JsonObject=Object }

shared class Server() extends Verticle() {
  
  shared actual void start() {
    value router_ = router.router(vertx);
    
    // We need cookies, sessions and request bodies
    router_.route().handler(cookieHandler.create().handle);
    router_.route().handler(bodyHandler.create().handle);
    router_.route().handler(sessionHandler.create(localSessionStore.create(vertx)).handle);
    
    // Simple auth service which uses a properties file for user/role info
    value authProvider = shiroAuth.create(vertx, ShiroAuthOptions {
      type = properties;
      config = JsonObject {
        "properties_path"->"auth.properties"
      };
    });
    
    // We need a user session handler too to make sure the user is stored in the session between requests
    router_.route().handler(userSessionHandler.create(authProvider).handle);
   
    // Any requests to URI starting '/private/' require login
    router_.route("/private/*").handler(redirectAuthHandler.create(authProvider, "/loginpage.html").handle);
   
    // Serve the static private pages from directory 'private'
    router_.route("/private/*").handler(staticHandler.create().setCachingEnabled(false).setWebRoot("private").handle);
    
    // Handles the actual login
    router_.route("/loginhandler").handler(formLoginHandler.create(authProvider).handle);
    
    // Implement logout
    router_.route("/logout").handler((context) {
      context.clearUser();
      // Redirect back to the index page
      context.response().putHeader("location", "/").setStatusCode(302).end();
    });
    
    // Serve the non private static pages
    router_.route().handler(staticHandler.create().handle);
    
    vertx.createHttpServer().requestHandler(router_.accept).listen(8080);
  }
}
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.auth.properties.PropertyFileAuthentication
import io.vertx.ext.web.handler.RedirectAuthHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.FormLoginHandler

def router = Router.router(vertx)

// We need sessions and request bodies
router.route().handler(BodyHandler.create())
router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))

// Simple auth service which uses a properties file for user/role info
def authn = PropertyFileAuthentication.create(vertx, "vertx-users.properties")

// Any requests to URI starting '/private/' require login
router.route("/private/*").handler(RedirectAuthHandler.create(authn, "/loginpage.html"))

// Serve the static private pages from directory 'private'
router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private"))

// Handles the actual login
router.route("/loginhandler").handler(FormLoginHandler.create(authn))

// Implement logout
router.route("/logout").handler({ context ->
  context.clearUser()
  // Redirect back to the index page
  context.response().putHeader("location", "/").setStatusCode(302).end()
})

// Serve the non private static pages
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router).listen(8080)

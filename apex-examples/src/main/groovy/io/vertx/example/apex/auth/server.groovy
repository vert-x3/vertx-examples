import io.vertx.groovy.ext.apex.Router
import io.vertx.groovy.ext.apex.handler.CookieHandler
import io.vertx.groovy.ext.apex.handler.SessionHandler
import io.vertx.groovy.ext.apex.sstore.LocalSessionStore
import io.vertx.groovy.ext.apex.handler.BodyHandler
import io.vertx.groovy.ext.auth.shiro.ShiroAuthService
import io.vertx.ext.auth.shiro.ShiroAuthRealmType
import io.vertx.groovy.ext.apex.handler.RedirectAuthHandler
import io.vertx.groovy.ext.apex.handler.StaticHandler
import io.vertx.groovy.ext.apex.handler.FormLoginHandler

def router = Router.router(vertx)

// We need cookies, sessions and request bodies
router.route().handler(CookieHandler.create())
router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))
router.route().handler(BodyHandler.create())

// Simple auth service which uses a properties file for user/role info
def authService = ShiroAuthService.create(vertx, ShiroAuthRealmType.PROPERTIES, [:])

// Any requests to URI starting '/private/' require login
router.route("/private/*").handler(RedirectAuthHandler.create(authService, "/loginpage.html"))

// Serve the static private pages from directory 'private'
router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private"))

// Handles the actual login
router.route("/loginhandler").handler(FormLoginHandler.create(authService))

// Implement logout
router.route("/logout").handler({ context ->
  context.session().logout()
  // Redirect back to the index page
  context.response().putHeader("location", "/").setStatusCode(302).end()
})

// Serve the non private static pages
router.route().handler(StaticHandler.create())

vertx.createHttpServer().requestHandler(router.&accept).listen(8080)

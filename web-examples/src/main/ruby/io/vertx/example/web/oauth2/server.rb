require 'vertx-web/router'
require 'vertx-web/cookie_handler'
require 'vertx-web/local_session_store'
require 'vertx-web/session_handler'
require 'vertx-auth-oauth2/github_auth'
require 'vertx-web/user_session_handler'
require 'vertx-web/o_auth2_auth_handler'
require 'vertx-auth-oauth2/access_token'
require 'vertx-web-templ-handlebars/handlebars_template_engine'
@CLIENT_SECRET = "3155eafd33fc947e0fe9f44127055ce1fe876704"
@CLIENT_ID = "57cdaa1952a3f4ee3df8"
@engine = VertxWebTemplHandlebars::HandlebarsTemplateEngine.create()
# To simplify the development of the web components we use a Router to route all HTTP requests
# to organize our code in a reusable way.
router = VertxWeb::Router.router($vertx)
# We need cookies and sessions
router.route().handler(&VertxWeb::CookieHandler.create().method(:handle))
router.route().handler(&VertxWeb::SessionHandler.create(VertxWeb::LocalSessionStore.create($vertx)).method(:handle))
# Simple auth service which uses a GitHub to authenticate the user
authProvider = VertxAuthOauth2::GithubAuth.create($vertx, @CLIENT_ID, @CLIENT_SECRET)
# We need a user session handler too to make sure the user is stored in the session between requests
router.route().handler(&VertxWeb::UserSessionHandler.create(authProvider).method(:handle))
# we now protect the resource under the path "/protected"
router.route("/protected").handler(&VertxWeb::OAuth2AuthHandler.create(authProvider).setup_callback(router.route("/callback")).add_authority("user:email").method(:handle))
# Entry point to the application, this will render a custom template.
router.get("/").handler() { |ctx|
  # we pass the client id to the template
  ctx.put("client_id", @CLIENT_ID)
  # and now delegate to the engine to render it.
  @engine.render(ctx, "views", "/index.hbs") { |res_err,res|
    if (res_err == nil)
      ctx.response().put_header("Content-Type", "text/html").end(res)
    else
      ctx.fail(res_err)
    end
  }
}
# The protected resource
router.get("/protected").handler() { |ctx|
  user = ctx.user()
  # retrieve the user profile, this is a common feature but not from the official OAuth2 spec
  user.user_info() { |res_err,res|
    if (res_err != nil)
      # request didn't succeed because the token was revoked so we
      # invalidate the token stored in the session and render the
      # index page so that the user can start the OAuth flow again
      ctx.session().destroy()
      ctx.fail(res_err)
    else
      # the request succeeded, so we use the API to fetch the user's emails
      userInfo = res

      # fetch the user emails from the github API

      # the fetch method will retrieve any resource and ensure the right
      # secure headers are passed.
      user.fetch("https://api.github.com/user/emails") { |res2_err,res2|
        if (res2_err != nil)
          # request didn't succeed because the token was revoked so we
          # invalidate the token stored in the session and render the
          # index page so that the user can start the OAuth flow again
          ctx.session().destroy()
          ctx.fail(res2_err)
        else
          userInfo['private_emails'] = res2.json_array()
          # we pass the client info to the template
          ctx.put("userInfo", userInfo)
          # and now delegate to the engine to render it.
          @engine.render(ctx, "views", "/advanced.hbs") { |res3_err,res3|
            if (res3_err == nil)
              ctx.response().put_header("Content-Type", "text/html").end(res3)
            else
              ctx.fail(res3_err)
            end
          }
        end
      }
    end
  }
}

$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

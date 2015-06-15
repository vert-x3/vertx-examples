require 'json'
require 'vertx-mongo/mongo_client'
require 'vertx-web/jade_template_engine'
require 'vertx-web/router'
require 'vertx-web/body_handler'
require 'vertx-web/static_handler'

# Create a mongo client using all defaults (connect to localhost and default port) using the database name "demo".
mongo = VertxMongo::MongoClient.create_shared($vertx, {
  'db_name' => "demo"
})

# In order to use a JADE template we first need to create an engine
jade = VertxWeb::JadeTemplateEngine.create()

# To simplify the development of the web components we use a Router to route all HTTP requests
# to organize our code in a reusable way.
router = VertxWeb::Router.router($vertx)

# Enable the body parser to we can get the form data and json documents in out context.
router.route().handler(&VertxWeb::BodyHandler.create().method(:handle))

# Entry point to the application, this will render a custom JADE template.
router.get("/").handler() { |ctx|
  # we define a hardcoded title for our application
  ctx.put("title", "Vert.x Web")

  # and now delegate to the engine to render it.
  jade.render(ctx, "templates/index") { |res_err,res|
    if (res_err == nil)
      ctx.response().put_header(Java::IoVertxCoreHttp::HttpHeaders::CONTENT_TYPE, "text/html").end(res)
    else
      ctx.fail(res_err)
    end
  }
}

# and now we mount the handlers in their appropriate routes

# Read all users from the mongo collection.
router.get("/users").handler() { |ctx|
  # issue a find command to mongo to fetch all documents from the "users" collection.
  mongo.find("users", {
  }) { |lookup_err,lookup|
    # error handling
    if (lookup_err != nil)
      ctx.fail(lookup_err)
      return
    end

    # now convert the list to a JsonArray because it will be easier to encode the final object as the response.
    json = [
    ]
    lookup.each do |o|
      json.push(o)
    end

    # since we are producing json we should inform the browser of the correct content type.
    ctx.response().put_header(Java::IoVertxCoreHttp::HttpHeaders::CONTENT_TYPE, "application/json")
    # encode to json string
    ctx.response().end(JSON.generate(json))
  }
}

# Create a new document on mongo.
router.post("/users").handler() { |ctx|
  # since jquery is sending data in multipart-form format to avoid preflight calls, we need to convert it to JSON.
  user = {
    'username' => ctx.request().get_form_attribute("username"),
    'email' => ctx.request().get_form_attribute("email"),
    'fullname' => ctx.request().get_form_attribute("fullname"),
    'location' => ctx.request().get_form_attribute("location"),
    'age' => ctx.request().get_form_attribute("age"),
    'gender' => ctx.request().get_form_attribute("gender")
  }

  # insert into mongo
  mongo.insert("users", user) { |lookup_err,lookup|
    # error handling
    if (lookup_err != nil)
      ctx.fail(lookup_err)
      return
    end

    # inform that the document was created
    ctx.response().set_status_code(201)
    ctx.response().end()
  }
}

# Remove a document from mongo.
router.delete("/users/:id").handler() { |ctx|
  # catch the id to remove from the url /users/:id and transform it to a mongo query.
  mongo.remove_one("users", {
    '_id' => ctx.request().get_param("id")
  }) { |lookup_err,lookup|
    # error handling
    if (lookup_err != nil)
      ctx.fail(lookup_err)
      return
    end

    # inform the browser that there is nothing to return.
    ctx.response().set_status_code(204)
    ctx.response().end()
  }
}

# Serve the non private static pages
router.route().handler(&VertxWeb::StaticHandler.create().method(:handle))

# start a HTTP web server on port 8080
$vertx.create_http_server().request_handler(&router.method(:accept)).listen(8080)

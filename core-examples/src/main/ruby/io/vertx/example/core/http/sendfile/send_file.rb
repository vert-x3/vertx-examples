
# In reality it's highly recommend you use Vert.x-Web for applications like this.

$vertx.create_http_server().request_handler() { |req|
  filename = nil
  if (req.path().==("/"))
    filename = "index.html"
  elsif (req.path().==("/page1.html"))
    filename = "page1.html"
  elsif (req.path().==("/page2.html"))
    filename = "page2.html"
  else
    req.response().set_status_code(404).end()
  end
  if (filename != nil)
    req.response().send_file(filename)
  end
}.listen(8080)

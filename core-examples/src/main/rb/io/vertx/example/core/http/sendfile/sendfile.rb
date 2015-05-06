
# In reality it's highly recommend you use Apex for applications like this.

$vertx.create_http_server().request_handler() { |req|
  filename = nil
  if (req.path().equals("/"))
    filename = "index.html"
  elsif (req.path().equals("/page1.html"))
    filename = "page1.html"
  elsif (req.path().equals("/page2.html"))
    filename = "page2.html"
  else
    req.response().set_status_code(404).end()
  end
  if (filename != nil)
    req.response().send_file(filename)
  end
}.listen(8080)


server = $vertx.create_http_server({
  'useAlpn' => true,
  'ssl' => true,
  'pemKeyCertOptions' => {
    'keyPath' => "server-key.pem",
    'certPath' => "server-cert.pem"
  }
})

server.request_handler() { |req|
  path = req.path()
  resp = req.response()
  if ("/".==(path))
    resp.push(:GET, "/script.js") { |ar_err,ar|
      if (ar_err == nil)
        puts "sending push"
        pushedResp = ar
        pushedResp.send_file("script.js")
      else
        # Sometimes Safari forbids push : "Server push not allowed to opposite endpoint."
      end
    }

    resp.send_file("index.html")
  elsif ("/script.js".==(path))
    resp.send_file("script.js")
  else
    puts "Not found #{path}"
    resp.set_status_code(404).end()
  end
}

server.listen(8443, "localhost") { |ar_err,ar|
  if (ar_err == nil)
    puts "Server started"
  else
    ar_err.print_stack_trace()
  end
}

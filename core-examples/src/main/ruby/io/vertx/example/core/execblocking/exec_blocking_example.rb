
$vertx.create_http_server().request_handler() { |request|

  # Let's say we have to call a blocking API (e.g. JDBC) to execute a query for each
  # request. We can't do this directly or it will block the event loop
  # But you can do this using executeBlocking:

  $vertx.execute_blocking(lambda { |promise|

    # Do the blocking operation in here

    # Imagine this was a call to a blocking API to get the result
    begin
      Java::JavaLang::Thread.sleep(500)
    rescue
    end

    result = "armadillos!"

    promise.complete(result)

  }) { |res_err,res|

    if (res_err == nil)

      request.response().put_header("content-type", "text/plain").end(res)

    else
      res_err.print_stack_trace()
    end
  }

}.listen(8080)


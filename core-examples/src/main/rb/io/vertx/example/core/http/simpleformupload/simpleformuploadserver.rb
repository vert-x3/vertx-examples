$vertx.create_http_server().request_handler() { |req|
  if (req.uri().equals("/"))
    # Serve the index page
    req.response().send_file("index.html")
  elsif (req.uri().starts_with?("/form"))
    req.set_expect_multipart(true)
    req.upload_handler() { |upload|
      upload.exception_handler() { |cause|
        req.response().set_chunked(true).end("Upload failed")
      }

      upload.end_handler() { |v|
        req.response().set_chunked(true).end("Successfully uploaded to #{upload.filename()}")
      }
      # FIXME - Potential security exploit! In a real system you must check this filename
      # to make sure you're not saving to a place where you don't want!
      # Or better still, just use Apex which controls the upload area.
      upload.stream_to_file_system(upload.filename())
    }
  else
    req.response().set_status_code(404)
    req.response().end()
  end
}.listen(8080)


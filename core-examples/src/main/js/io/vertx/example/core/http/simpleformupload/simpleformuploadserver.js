vertx.createHttpServer().requestHandler(function (req) {
  if (req.uri() == "/") {
    // Serve the index page
    req.response().sendFile("index.html");
  } else if (req.uri().startsWith("/form")) {
    req.setExpectMultipart(true);
    req.uploadHandler(function (upload) {
      upload.exceptionHandler(function (cause) {
        req.response().setChunked(true).end("Upload failed");
      });

      upload.endHandler(function (v) {
        req.response().setChunked(true).end("Successfully uploaded to " + upload.filename());
      });
      // FIXME - Potential security exploit! In a real system you must check this filename
      // to make sure you're not saving to a place where you don't want!
      // Or better still, just use Vert.x-Web which controls the upload area.
      upload.streamToFileSystem(upload.filename());
    });
  } else {
    req.response().setStatusCode(404);
    req.response().end();
  };
}).listen(8080);


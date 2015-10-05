# WebPack example (a modified version of https://github.com/kastork/vertx-client-webpack-reproducer)

The server is just enough to serve the client and do a minimal thing on the event bus.
It runs http server on port 8080.

## Client

The client is a ReactJS/Apt/Vertx app that's built with Webpack.

When the build is working, you can visit

http://localhost:8080/index.html

The page will show a time readout that is updated by the vertx server every second.

There are build scripts in the package.json, so build in the usual NPM way...

```bash
cd client
npm install
npm run devbuild
```

This will build the client and put it in the server's webroot.
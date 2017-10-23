### Vert.x + React.js SSR

This projects aims at showing [Vert.x](http://vertx.io) and [react.js](https://facebook.github.io/react/) server side rendering capabilities.

To run the project, simply run `npm install && npm start` from the project's root directory, then point your browser at [http://localhost:8080](http://localhost:8080) to get started.

The example shows how to reuse `react.js` code in your server and use it to prerender the start HTML page.

It also shows how to use `webpack` and `babel` in order to overcome the limitations of Nashorn with respect to javascript language level.

The client code (Browser) lives under `src/client` and the server code under `src/server`. All code under `src/shared` is shared by both client and server (typically your react application logic).

For a more productive development workflow, the `package.json` file also has a `watch` script using `vert.x`, if run, it will start your application and on file save it will reload the application for you.

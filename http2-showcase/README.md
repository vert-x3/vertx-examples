### Vert.x HTTP/2 showcase

This projects aims at showing [Vert.x](http://vertx.io) [HTTP/2](https://en.wikipedia.org/wiki/HTTP/2) capabilities.

To run the project, simply run `./gradlew run` from the project's root directory, then point your browser at [http://localhost:8080](http://localhost:8080) to get started.

Two HTTP servers are running:

* An HTTP/1.1 server running on http://localhost:8080
* An HTTP/2 server running on https://localhost:8443

They both are running the exact same code, the only difference is that the second one has been declared with a specific option : `setUseAlpn(true)`.

By default, when running on your local machine, a latency of 100ms is arbitrarily simulated. So that you can figure out the difference it makes in a real-life environment.
You can change the latency by clicking on the links on the right of the image, or by adding a query parameter named `latency` to the page's URL.

e.g. [http://localhost:8080/image.hbs?latency=999](http://localhost:8080/image.hbs?latency=999)

The project uses :
* [Vert.x](http://vertx.io)
* [Vert.x web](http://vertx.io/docs/#web)
* [Handlebars](https://github.com/jknack/handlebars.java) as template engine

Licensed under [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0)

Any contribution is welcome.

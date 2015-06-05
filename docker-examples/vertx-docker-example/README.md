# Vert.x Docker Example

This project builds a docker image launching a very simple Vert.x verticle.
 
# Build the image

To build the docker image, just launch:

`mvn clean package`

Notice that you need to have docker installed on your machine.

# Launching the image

Just launch:
 
`docker run -p 8080:8080 -i -t vertx/vertx3-example`

You should get a `Hello World` message on `http://localhost:8080`.

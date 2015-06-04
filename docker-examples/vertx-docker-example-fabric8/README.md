# Vert.x Docker Example for Fabric8

This project builds a docker image launching a very simple Vert.x verticle that you can deploy using Fabric8
 
# Build the image

To build the docker image, just launch:

`mvn clean package`

Notice that you need to have docker installed on your machine.

# Deployment on Fabric8

The build creates the `kubernates.json` file with the required metadata.

First, deploy the image on the Docker registry manage by fabric8:

`docker push $DOCKER_REGISTRY/vertx/vertx3-example-fabric8`

Then, apply it:

`mvn io.fabric8:fabric8-maven-plugin:2.1.4:apply`

That's all.

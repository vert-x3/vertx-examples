# Vert.x on Cloud Foundry

This demo shows a very simple hello world Vert.x project for [Cloud Foundry](https://www.cloudfoundry.org/). It has a simple HTTP server which simply serves the /webroot/index.html.

This demo uses [Vert.x 3.5.0](http://vertx.io) and is packed using the official [CloudFoundry java-buildpack](https://github.com/cloudfoundry/java-buildpack). The `manifest.yml` specifies the app's memory to be 768MB because any lower and the java-buildpack throws an error that it can't allocate enough heap space.

The environment variable `PORT` is normally provided by your CloudFoundry service, and therefore can change when being deployed. Otherwise the default port is `8080`.

The `webroot` is located under `src/main/java/resources` so it can be packaged into the JAR file. If the `webroot` is not packaged inside the JAR file like this, then we can't deploy it easily to CloudFoundry using the java-buildpack. A custom buildpack would have to be created instead. But that's beyond the scope of this demo.

This demo also uses Gradle+shadowJar plugin to build the application and all it’s dependencies into a single "fat" jar.

## Prerequisites

* [Java 1.8](https://www.java.com/download/)
* [CloudFoundry CLI](https://docs.cloudfoundry.org/cf-cli/install-go-cli.html)
* A Cloud service provider, such as [IBM Cloud](https://www.ibm.com/cloud/)

## Build fat jar

1. Run build `./gradlew clean build`
1. Test app `java -jar build/libs/vertx-cf-0.1.0-all.jar`
1. Visit [http://localhost:8080/](http://localhost:8080/) to see the app running.

## Deploy to Cloud Foundry

1. Deploy `cf push -f manifest.yml myapp`
1. Run `cf apps` to see the app running. Visit the url provided.

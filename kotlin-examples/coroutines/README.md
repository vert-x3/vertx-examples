# Vert.x Kotlin Coroutines Example

A movie rating REST application written in [Kotlin](https://kotlinlang.org/) to demonstrate how Kotlin coroutines can be used with Vert.x.

Kotlin Coroutines enables to write asynchronous code with sequential statements, the function `rateMovie` is the best example
showing two asynchronous SQL client operations could be executed sequentially just like writing synchronous code.

## Setup the database

Setup a MySQL database server with Docker

```
docker run -it --rm=true --name vertx-coroutine-example-test -e MYSQL_USER=vertx -e MYSQL_PASSWORD=vertx -e MYSQL_ROOT_PASSWORD=my-secret-pw -e MYSQL_DATABASE=vertx_example -p 3306:3306 mysql:8
```

## Running from the IDE

Run the main function from the IDE

## Running as a far jar

```
> mvn package
> java -jar target/kotlin-coroutines-examples.jar
```

## Running from the CLI

```
> vertx run src/main/kotlin/movierating/App.kt
```

## API

The application exposes a REST API for getRating movies:

You can know more about a movie

```
> curl http://localhost:8080/movie/starwars
{"id":"starwars","title":"Star Wars"}
```

You can get the current rating of a movie:

```
> curl http://localhost:8080/getRating/indianajones
{"id":"indianajones","getRating":5}
```

Finally you can rate a movie

```
> curl -X POST http://localhost:8080/rateMovie/starwars?getRating=4
```

# Vert.x Kotlin Coroutines Example

A movie rating REST application written in [Kotlin](https://kotlinlang.org/) to demonstrate how it can Kotlin
coroutines can be used with Vert.x.

Coroutines enables to write asynchronous code with sequential statements.

## Running from the IDE

Run the main function from the IDE

## Running as a far jar

```
> mvn package
> java -jar target/service.jar
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
{"id":"indianajones","rating":5}
```

Finally you can rate a movie

```
> curl -X POST http://localhost:8080/rateMovie/starwars?rating=4
```

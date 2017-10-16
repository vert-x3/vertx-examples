# Vert.x Kotlin Coroutines Example

A movie getRating REST application written in [Kotlin](https://kotlinlang.org/) to demonstrate how it can Kotlin
coroutines can be used with Vert.x.  

Coroutines enables to write asynchronous code with sequential statements, the function `rateMovie` is the best example
showing two SQL client operations wrapped in a `use` block managing the SQL connection.

## Running from the IDE

Run the main function from the IDE

## Running as a far jar

```
> mvn package
> java -jar target/kotlin-coroutines-examples.jar
```

## Running from the CLI

Copy hsqldb jar to $VERTX_HOME/lib

```
> cp $HOME/.m2/repository/org/hsqldb/hsqldb/2.4.0/hsqldb-2.4.0.jar $VERTX_HOME/lib/
> vertx run src/main/kotlin/movierating/App.kt
```

## API

The application exposes a REST API for getRating movies:

You can know more about a movie

```
> curl http://localhost:8080/movie/starwars
{"id":"starwars","title":"Star Wars"}
```

You can get the current getRating of a movie:

```
> curl http://localhost:8080/getRating/indianajones
{"id":"indianajones","getRating":5}
```

Finally you can rateMovie a movie

```
> curl -X POST http://localhost:8080/rateMovie/starwars?getRating=4
```

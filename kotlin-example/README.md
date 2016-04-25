# Vert.x Kotlin Example

Basic JSON API written in [Kotlin](https://kotlinlang.org/) to demonstrate how it can be used with Vert.x.

This project is build on Gradle and is ready to be deployed on Heroku, hence it combines parts of the following 
examples:

- Gradle:
    - [gradle_redeploy](https://github.com/vert-x3/vertx-examples/tree/master/gradle-redeploy)
    - [gradle_simplest](https://github.com/vert-x3/vertx-examples/tree/master/gradle-simplest)
    - [gradle_verticles](https://github.com/vert-x3/vertx-examples/tree/master/gradle-verticles)
- Heroku:
    - [heroku-example](https://github.com/vert-x3/vertx-examples/tree/master/heroku-example)

## Running

The most convenient way to run the project during development is as follows:

```
./gradlew run
```

That way, any changes to the classes will be picked up and re-deployed automatically.

## Deploying

The application can either be deployed manually (with a jar file) or to [Heroku](https://www.heroku.com/).

### Manually

You can generate a single "shadow jar" (more on that [here](https://github.com/johnrengelman/shadow)) as follows:

```
./gradlew shadowJar
```

The jar file can now be found in `./build/libs/app-shadow.jar` and deployed on your custom platform.

### Heroku

This project includes a custom `Procfile` to simplify deployment to Heroku, however you would need to place the 
contents of this example into a separate Git repository first (because the global [`Procfile`](https://github.com/vert-x3/vertx-examples/blob/master/Procfile) 
in the root directory points to the `heroku-example`).
 
Once in your new Git repository, install the [Heroku Toolbelt](https://toolbelt.heroku.com/) and issue the 
following:

```
heroku login
heroku create
git push heroku master
```

*Note:* All your changes should be committed to Git before issuing the last command.

# Vert.x Kotlin Coroutines Example

JSON API written in [Kotlin](https://kotlinlang.org/) to demonstrate how it can be Kotlin coroutines can be used with Vert.x.

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

The jar file can now be retrieved from `./build/libs/app-shadow.jar` and deployed onto your preferred platform.

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

*Note:* All your changes must be committed to Git before issuing the last command.

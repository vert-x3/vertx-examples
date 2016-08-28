# Vert.x Heroku Example

This project shows how to deploy a Vert.x 3 applications to Heroku. The same application can be deployed using 3 approaches:

* Using a one click badge
* Using the maven plugin
* Using the git interface

If you use a fat-jar then deploying on heroku is as simple as one click. The only requirement is to create the Heroku specific [Procfile](../Procfile) with a `Dyno` of type web.

## To deploy with the one click mode:

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://dashboard.heroku.com/new?&template=https%3A%2F%2Fgithub.com%2Fvert-x3%2Fvertx-examples)

## Use the Heroku Toolbelt

Follow these steps, after installing the [Heroku Toolbelt](https://toolbelt.heroku.com/), to deploy with Maven:

```sh-session
$ git clone https://github.com/vert-x3/vertx-examples
$ heroku create
$ mvn -pl heroku-example package heroku:deploy
```

## Git

Follow these steps to deploy with Git.

```sh-session
$ git clone https://github.com/vert-x3/vertx-examples
$ heroku create
$ heroku config:set MAVEN_CUSTOM_OPTS="-DskipTests -pl heroku-example"
$ git push heroku master
```

When creating a project of your own, you'll need to borrow from the [`Procfile`](https://github.com/vert-x3/vertx-examples/blob/master/Procfile) in the root directory of this project, and the `MAVEN_CUSTOM_OPTS` will only be necessary if your app is not the primary module of your Maven project.

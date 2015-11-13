# Vert.x Heroku Example

This project shows how to deploy a Vert.x 3 applications to Heroku. The same application can be deployed using 3 approaches:

* Using a one click badge
* Using the maven plugin
* Using the git interface

If you use a fat-jar then deploying on heroku is as simple as one click. The only requirement is to create the Heroku specific [Procfile](./Procfile) with a `Dyno` of type web.

## To deploy with the one click mode:

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

## Use the Heroku Toolbelt

Follow these steps, after installing the [Heroku Toolbelt](https://toolbelt.heroku.com/), to deploy with Maven:

```sh-session
$ git clone https://github.com/vert-x3/vertx-examples
$ cd heroku-examples
$ heroku create
$ mvn package heroku:deploy -Dheroku.appName=<appName>
```

## Git

Follow these steps to deploy with Git.

```sh-session
$ git clone https://github.com/vert-x3/vertx-examples
$ heroku create
$ git push heroku master
```
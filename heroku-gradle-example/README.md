# Vert.x Heroku Example

This project shows how to deploy a Vert.x 3 applications to Heroku. The same application can be deployed using 3 approaches:

you must to execute

heroku config:set GRADLE_TASK="shadowJar"

to configure task build.

## Git

Follow these steps to deploy with Git.

```sh-session
$ git clone https://github.com/vert-x3/vertx-examples
$ heroku create
$ heroku config:set GRADLE_TASK="shadowJar"
$ git push heroku master
```


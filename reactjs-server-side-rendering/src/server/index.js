const Router = require("vertx-web-js/router")
const StaticHandler = require("vertx-web-js/static_handler")
const posts = require('../shared/posts')

import React from 'react';
import {renderToString} from 'react-dom/server'
import {match, RouterContext} from 'react-router'
import routes from '../shared/components/routes'

const app = Router.router(vertx);

app.get('/api/post').handler((ctx) => {
  ctx.response()
    .putHeader("content-type", "application/json")
    .end(JSON.stringify(posts));
});

app.get('/api/post/:id/*').handler((ctx) => {
  const id = ctx.request().getParam('id')

  const post = posts.filter(p => p.id == id)

  if (post) {
    ctx.response()
      .putHeader("content-type", "application/json")
      .end(JSON.stringify(post[0]))
  } else {
    ctx.fail(404);
  }
});

app.get().handler((ctx) => {
  match({routes: routes, location: ctx.request().uri()}, (err, redirect, props) => {

    if (err) {
      ctx.fail(err.message);
    } else if (redirect) {
      ctx.response()
        .putHeader("Location", redirect.pathname + redirect.search)
        .setStatusCode(302)
        .end();
    } else if (props) {
      const routerContextWithData = (
        <RouterContext
          {...props}
          createElement={(Component, props) => {
            return <Component posts={posts} {...props} />
          }}
        />
      );
      const appHtml = renderToString(routerContextWithData)

      ctx.response()
        .putHeader("content-type", "text/html")
        .end(`<!DOCTYPE html>
              <html lang="en">
              <head>
                <link rel="stylesheet" href="https://unpkg.com/wingcss" />
                <meta charset="UTF-8">
                <title>Universal Blog</title>
              </head>
              <body>
                <div id="app">${appHtml}</div>
                <script src="/bundle.js"></script>
              </body>
              </html>`)
    } else {
      ctx.next()
    }
  });
});

app.get().handler(StaticHandler.create().handle)

vertx.createHttpServer().requestHandler(app.accept).listen(8080)

console.log('Server listening: http://127.0.0.1:8080/')
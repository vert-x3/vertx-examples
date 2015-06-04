/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package io.vertx.example.web.sessions;

/**
 * Created by tim on 17/04/15.
 */

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

public class Example
  implements Runnable
{
  private static final Logger logger =
    LoggerFactory.getLogger(Example.class);
  private Vertx vertx;

  public static void main(String[] args)
  {
    new Example(args).run();
  }

  public Example(String[] args)
  {
  }

  @Override
  public void run()
  {
    VertxOptions options;

    options = new VertxOptions();
    Vertx.clusteredVertx(options, (AsyncResult<Vertx> event) -> {
      if (event.succeeded()) {
        vertx = event.result();
        startHttpServer();
      }
      else {
        logger.error("failed to obtain vertx object", event.cause());
      }
    });
  }

  private void startHttpServer()
  {
    SessionStore store;
    Router router;

    store = ClusteredSessionStore.create(vertx);

    router = Router.router(vertx);
    router.route().handler(CookieHandler.create());
    router.route().handler(SessionHandler.create(store));
    router.route().handler(this::doSomething);

    vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(8080, (AsyncResult<HttpServer> event) -> {
        if (event.succeeded()) {
          logger.info("HTTP server started successfully");
        }
        else {
          logger.error("HTTP server failed to start",
            event.cause());
        }
      });
  }

  private void doSomething(RoutingContext context)
  {
    Integer count;

    if ((count = context.session().get("count")) == null) {
      count = 0;
    }
    count += 1;
    context.session().put("count", count);
    context.response()
      .setStatusCode(200)
      .putHeader("Content-type", "text/plain")
      .end("count = " + count + "\n");
  }
}

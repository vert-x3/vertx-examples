/*
 * Copyright (c) 2024, SAP SE
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *
 */

package io.vertx.example.openapi;

import io.vertx.core.Vertx;
import io.vertx.openapi.validation.RequestValidator;

public class ValidateRequest {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    CreateContract.createContract(vertx).map(openAPIContract -> RequestValidator.create(vertx, openAPIContract))
      .onSuccess(requestValidator -> {
        System.out.println("RequestValidator created");
        vertx.createHttpServer().requestHandler(req ->
          requestValidator.validate(req).onFailure(t -> {
            System.out.println("Request is invalid: " + t.getMessage());
            req.response().setStatusCode(400).end(t.getMessage());
          }).onSuccess(validatedRequest -> System.out.println("Request is valid"))
        ).listen(8080, "localhost");
        System.out.println("HttpServer created");
      }).onFailure(t -> {
        t.printStackTrace();
        System.exit(1);
      });
  }
}

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
import io.vertx.core.Future;
import io.vertx.openapi.contract.OpenAPIContract;

import java.nio.file.Path;
import java.util.Map;

public class CreateContract {
  private static final Path RESOURCES = Path.of("openapi-examples/src/main/resources").toAbsolutePath();

  public static Future<OpenAPIContract> createContract(Vertx vertx) {
    return OpenAPIContract.from(vertx, RESOURCES.resolve("petstore.yaml").toString());
  }

  public static Future<OpenAPIContract> createContractAdditionalFiles(Vertx vertx) {
    Path additionalFilesDir = RESOURCES.resolve("additional_files");
    String petstore = additionalFilesDir.resolve("petstore.yaml").toString();

    Map<String, String> additionalFiles = Map.of(
      "https://example.com/petstore", additionalFilesDir.resolve("components.yaml").toString()
    );

    return OpenAPIContract.from(vertx, petstore, additionalFiles);
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    createContract(vertx).compose(contract -> {
      System.out.println("Contract created");
      printContract(contract);

      return createContractAdditionalFiles(vertx);
    }).onSuccess(contract -> {
      System.out.println("Contract with additional files created");
      printContract(contract);

      System.exit(0);
    }).onFailure(t -> {
      t.printStackTrace();
      System.exit(1);
    });
  }

  private static void printContract(OpenAPIContract contract) {
    contract.getPaths().forEach(path -> System.out.println("Path: " + path));
  }
}

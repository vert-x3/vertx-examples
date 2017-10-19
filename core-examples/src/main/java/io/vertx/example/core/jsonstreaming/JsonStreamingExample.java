/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.example.core.jsonstreaming;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.example.util.Runner;

import java.util.concurrent.atomic.AtomicInteger;

import static io.vertx.core.parsetools.JsonEventType.*;

/**
 * @author Thomas Segismont
 */
public class JsonStreamingExample extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(JsonStreamingExample.class);
  }

  @Override
  public void start() throws Exception {
    vertx.fileSystem().open("large.json", new OpenOptions(), ar -> {
      if (ar.succeeded()) {
        AsyncFile asyncFile = ar.result();

        AtomicInteger counter = new AtomicInteger();

        JsonParser jsonParser = JsonParser.newParser(asyncFile);
        jsonParser.objectValueMode()
          .exceptionHandler(t -> {
            t.printStackTrace();
            asyncFile.close();
          })
          .endHandler(v -> {
            System.out.println("Done!");
            asyncFile.close();
          }).handler(event -> {
          if (event.type() == VALUE) {
            DataPoint dataPoint = event.mapTo(DataPoint.class);
            if (counter.incrementAndGet() % 100 == 0) {
              System.out.println("DataPoint = " + dataPoint);
            }
          }
        });
      } else {
        ar.cause().printStackTrace();
      }
    });
  }
}

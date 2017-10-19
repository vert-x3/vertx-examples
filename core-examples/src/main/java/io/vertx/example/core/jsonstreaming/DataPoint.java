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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Thomas Segismont
 */
public class DataPoint {

  private final long timestamp;
  private final double value;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public DataPoint(@JsonProperty("ts") long timestamp, @JsonProperty("val") double value) {
    this.timestamp = timestamp;
    this.value = value;
  }

  @JsonProperty("ts")
  public long getTimestamp() {
    return timestamp;
  }

  @JsonProperty("val")
  public double getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "DataPoint{" + "timestamp=" + timestamp + ", value=" + value + '}';
  }
}

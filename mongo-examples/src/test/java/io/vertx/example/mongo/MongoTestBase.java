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

package io.vertx.example.mongo;

import io.vertx.ext.unit.junit.VertxUnitRunner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * @author Erwin de Gier
 */
@RunWith(VertxUnitRunner.class)
public abstract class MongoTestBase {

  private static MongodExecutable exe;

  @BeforeClass
  public static void startMongo() throws Exception {
    IMongodConfig config = new MongodConfigBuilder().version(Version.Main.PRODUCTION).net(new Net(27017, Network.localhostIsIPv6())).build();
    exe = MongodStarter.getDefaultInstance().prepare(config);
    exe.start();

  }

  @AfterClass
  public static void stopMongo() {
    if (exe != null) {
      exe.stop();
    }
  }

}
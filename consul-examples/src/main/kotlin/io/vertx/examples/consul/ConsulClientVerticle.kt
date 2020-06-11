package io.vertx.examples.consul

import io.vertx.ext.consul.ConsulClient

class ConsulClientVerticle : io.vertx.core.AbstractVerticle()  {
  override fun start() {
    var consulClient = ConsulClient.create(vertx)
    consulClient.putValue("key11", "value11").compose<Any>({ v ->
      println("KV pair saved")
      return consulClient.getValue("key11")
    }).onComplete({ ar ->
      if (ar.succeeded()) {
        println("KV pair retrieved")
        println(ar.result().value)
      } else {
        ar.cause().printStackTrace()
      }
    })
  }
}

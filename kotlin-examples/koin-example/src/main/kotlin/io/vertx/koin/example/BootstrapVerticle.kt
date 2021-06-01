package io.vertx.koin.example

import io.vertx.core.AbstractVerticle
import io.vertx.kotlin.core.deploymentOptionsOf
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin

class BootstrapVerticle : AbstractVerticle() {
  override fun start() {
    startKoin(modules)
    vertx.registerVerticleFactory(KoinVerticleFactory)
    vertx.deployVerticle(
      "${KoinVerticleFactory.prefix()}:${GreeterVerticle::class.java.canonicalName}",
      deploymentOptionsOf(instances = 4)
    )
  }
}

val modules = listOf(module {
  factory { Greeter() }
  factory { GreeterVerticle(greeter = get()) }
})

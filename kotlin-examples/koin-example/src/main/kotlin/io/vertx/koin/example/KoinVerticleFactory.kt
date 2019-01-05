package io.vertx.koin.example

import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

object KoinVerticleFactory : VerticleFactory, KoinComponent {
  override fun prefix(): String = "koin"

  override fun createVerticle(verticleName: String, classLoader: ClassLoader): Verticle {
    return get(clazz = Class.forName(verticleName.substringAfter("koin:")).kotlin)
  }
}

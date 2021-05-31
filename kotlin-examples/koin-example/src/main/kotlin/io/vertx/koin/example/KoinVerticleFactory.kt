package io.vertx.koin.example

import io.vertx.core.Promise
import io.vertx.core.Verticle
import io.vertx.core.spi.VerticleFactory
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import java.util.concurrent.Callable

object KoinVerticleFactory : VerticleFactory, KoinComponent {
  override fun prefix(): String = "koin"

  override fun createVerticle(verticleName: String, classLoader: ClassLoader, promise: Promise<Callable<Verticle>>) {
    return get(clazz = Class.forName(verticleName.substringAfter("koin:")).kotlin)
  }
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.10"
  id("io.vertx.vertx-plugin") version "0.3.1"
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("io.vertx:vertx-core")
  implementation("io.vertx:vertx-lang-kotlin")
  implementation("org.koin:koin-core:1.0.2")
  implementation("org.slf4j:jcl-over-slf4j:1.7.20")
  implementation("ch.qos.logback:logback-classic:1.1.7")
}

vertx {
  mainVerticle = "io.vertx.koin.example.BootstrapVerticle"
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
    }
  }
}

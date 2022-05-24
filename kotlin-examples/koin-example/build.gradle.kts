import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.5.10"
  id("io.vertx.vertx-plugin") version "1.2.0"
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
  vertxVersion = "4.3.1"
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
    }
  }
}

tasks.wrapper {
  distributionType = Wrapper.DistributionType.ALL
  gradleVersion = "6.8.3"
}

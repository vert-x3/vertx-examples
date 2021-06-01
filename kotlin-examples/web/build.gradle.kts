import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("com.github.johnrengelman.shadow") version "7.0.0"
  kotlin("jvm") version "1.5.10"
  application
}

repositories {
  mavenCentral()
}

dependencies {
  val vertxVersion = "4.0.0.CR2"
  implementation(kotlin("stdlib-jdk8"))
  implementation("io.vertx:vertx-web:${vertxVersion}")
  implementation("io.vertx:vertx-lang-kotlin:${vertxVersion}")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.12.3")
}

val main = "io.vertx.core.Launcher"
val mainVerticleName = "io.vertx.example.MainVerticle"

application {
  mainClass.set(main)
}

// Customize the run task
val run by tasks.named<JavaExec>("run") {
  args = listOf("run", mainVerticleName,
    "--launcher-class=${main}",
    "--redeploy=src/**/*.*",
    "--on-redeploy=./gradlew classes")
}

tasks.named<ShadowJar>("shadowJar") {
  archiveBaseName.set("app")
  archiveClassifier.set("shadow")
  manifest {
    attributes(mapOf("Main-Class" to mainVerticleName))
  }
  mergeServiceFiles {
    include("META-INF/services/io.vertx.core.spi.VerticleFactory")
  }
}

// Heroku relies on the 'stage' task to deploy.
tasks.create("stage") {
  dependsOn("shadowJar")
}

tasks.wrapper {
  distributionType = Wrapper.DistributionType.ALL
  gradleVersion = "7.0.2"
}

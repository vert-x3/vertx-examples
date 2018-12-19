import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "4.0.3"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.vertx:vertx-core:3.6.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "io.vertx.core.Launcher"
}

val mainVerticleName = "io.vertx.example.HelloWorldVerticle"
val watchForChange = "src/**/*.java"
val doOnChange = "${projectDir}/gradlew classes"

tasks {
    getByName<JavaExec>("run") {
        args = listOf("run", mainVerticleName, "--redeploy=${watchForChange}", "--launcher-class=${application.mainClassName}", "--on-redeploy=${doOnChange}")
    }

    withType<ShadowJar> {
        classifier = "fat"
        manifest {
            attributes["Main-Verticle"] = mainVerticleName
        }
        mergeServiceFiles {
            include("META-INF/services/io.vertx.core.spi.VerticleFactory")
        }
    }
}

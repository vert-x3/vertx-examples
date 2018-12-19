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
    mainClassName = "io.vertx.example.HelloWorldEmbedded"
}

tasks {
    withType<ShadowJar> {
        classifier = "fat"
        mergeServiceFiles {
            include("META-INF/services/io.vertx.core.spi.VerticleFactory")
        }
    }
}

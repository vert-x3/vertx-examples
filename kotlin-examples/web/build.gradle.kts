@file:Suppress("PropertyName")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

val MAIN_VERTICLE = "io.vertx.example.MainVerticle"
val MAIN_CLASS = "io.vertx.core.Launcher"
val VERTX_VER = "3.5.0"
val SHADOW_VER = "2.0.1"

if (!JavaVersion.current().isJava8Compatible) IllegalStateException("Java 8 required!")

// Section for setting up repositories and dependencies for plugins.
buildscript {
    // Should be able to setup extra project metadata here however it is limited by scope for some strange reason.
    // Can setup metadata by doing the following, eg: extra["kotlin-ver"] = "1.1.3"
    extra["kotlin-ver"] = "1.1.51"

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        // Have to manually specify the version due to a Gradle Kotlin DSL bug.
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.1.3")
        // Have to manually specify the version due to a Gradle Kotlin DSL bug.
        classpath("com.github.jengelman.gradle.plugins:shadow:2.0.1")
    }
}

val KOTLIN_VER = "${extra["kotlin-ver"]}"

apply {
    plugin("com.github.johnrengelman.shadow")
}

// Type safe way to apply plugins.
plugins {
    java
    application
    // Apply the Kotlin JVM Gradle plugin. Version has to be specified manually due to a Gradle limitation.
    // Refer to the documentation on "plugins" block limitations: https://docs.gradle.org/current/userguide/plugins.html#plugins_dsl_limitations
    kotlin(module = "jvm", version = "1.1.51")
}

// Setup repositories for libraries/frameworks.
repositories {
    jcenter()
    mavenCentral()
    maven {
        url = URI("https://oss.sonatype.org/content/repositories/iovertx-3684/")
    }
}

// Setup dependencies for libraries/frameworks.
dependencies {
    compile("io.vertx:vertx-core:$VERTX_VER")
    compile("io.vertx:vertx-web:$VERTX_VER")
    compile(kotlin(module = "stdlib-jre8", version = KOTLIN_VER))
}

application { mainClassName = MAIN_CLASS }
java { setTargetCompatibility("1.8") }

tasks {
    val watcherPath = "src/**/*.kt"
    val watcherAction = "./gradlew classes"
    val shadowJar: ShadowJar by getting
    val compileKotlin: KotlinCompile by getting
    val run: JavaExec by getting

    compileKotlin.apply {
        kotlinOptions.jvmTarget = "1.8"
        doLast {
            println("Finished compiling.")
        }
    }

    run.args(
        "run",
        MAIN_VERTICLE,
        "--launcher-class=$MAIN_CLASS",
        "--redeploy=$watcherPath",
        "--on-redeploy=$watcherAction"
    )

    // Naming and packaging settings for the "shadow jar".
    shadowJar.apply {
        baseName = project.name
        classifier = ""
        manifest {
            attributes["Main-Verticle"] = MAIN_VERTICLE
            attributes["Main-Class"] = MAIN_CLASS
        }

        mergeServiceFiles {
            include("META-INF/services/io.vertx.core.spi.VerticleFactory")
        }
    }

    // Heroku relies on the 'stage' task to deploy.
    task("stage") {
        dependsOn(shadowJar)
    }
}

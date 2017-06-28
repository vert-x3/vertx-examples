import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

val MAIN_VERTICLE = "io.vertx.example.MainVerticle"
val MAIN_CLASS = "io.vertx.core.launcher"
val KOTLIN_VER = "1.1.3"
val VERTX_VER = "3.4.2"
val SHADOW_VER = "2.0.1"

if (!JavaVersion.current().isJava8Compatible) {
    IllegalStateException("Java 8 required!")
}

// Section for setting up repositories and dependencies for plugins.
buildscript {
    // Should be able to setup extra project metadata here however it is limited by scope for some strange reason.
    // Can setup metadata by doing the following, eg: extra["kotlin-ver"] = "1.1.3"
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

apply {
    plugin("com.github.johnrengelman.shadow")
}

plugins {
    java
    application
    kotlin("jvm")
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
    compile("org.jetbrains.kotlin:kotlin-stdlib-jre8:$KOTLIN_VER")
    compile("org.jetbrains.kotlin:kotlin-runtime:$KOTLIN_VER")
}

application { mainClassName = MAIN_CLASS }

java { setTargetCompatibility("1.8") }

tasks {
    val shadowJar: ShadowJar by getting
    val compileKotlin: KotlinCompile by getting

    compileKotlin.apply {
        kotlinOptions.jvmTarget = "1.8"
        doLast {
            println("Finished compiling.")
        }
    }

    // Naming and packaging settings for the "shadow jar".
    shadowJar.apply {
        baseName = "app"
        classifier = "shadow"
        manifest {
            attributes["Main-Verticle"] = MAIN_VERTICLE
            attributes["Main-Class"] = MAIN_CLASS
        }
        mergeServiceFiles {
            include("META-INF/services/io.vertx.core.spi.VerticleFactory")
        }
    }

    // Setup all tasks of type "Jar".
    withType<Jar> {
        dependsOn("classes")
        manifest {
            attributes["Main-Verticle"] = MAIN_VERTICLE
            attributes["Main-Class"] = MAIN_CLASS
        }
        from("src/main/kotlin", configurations.compile.map { zipTree(it) })
    }

    task(name = "wrapper", type = Wrapper::class) {
        gradleVersion = "4.0"
    }

    // Heroku relies on the 'stage' task to deploy.
    task("stage") {
        dependsOn(shadowJar)
    }

    // TODO: Find out how to customise the "run" task to setup program arguments.
    task(name = "launchVerticle", type = JavaExec::class) {
        dependsOn("jar")
        classpath = java.sourceSets.asMap["main"]?.runtimeClasspath
        main = MAIN_CLASS
        args.add("run")
        args.add(MAIN_VERTICLE)
        args.add("--launcher-class=$MAIN_VERTICLE")
        // Redeploy watcher.
        args.add("\\\"--redeploy=src/**/*.*\\\"")
        args.add("--on-redeploy=./gradlew classes")
    }
}

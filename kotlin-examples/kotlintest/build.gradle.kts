@file:Suppress("PropertyName")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.net.URI
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val VERTX_VER = "3.6.0"
val KOTLIN_TEST_VER = "2.0.6"


group = "org.digieng"
version = "0.1-SNAPSHOT"

buildscript {
    extra["shadow-ver"] = "2.0.1"
    extra["kotlin-ver"] = "1.1.51"

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:${extra["shadow-ver"]}")
    }
}

val KOTLIN_VER = "${extra["kotlin-ver"]}"

plugins {
    kotlin(module = "jvm", version = "1.1.51")
    application
}

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = URI.create("https://oss.sonatype.org/content/repositories/iovertx-3717/")
    }
}

apply {
    plugin("com.github.johnrengelman.shadow")
}

dependencies {
    compile(kotlin(module = "stdlib-jre8", version = KOTLIN_VER))
    compile("io.vertx:vertx-web-client:$VERTX_VER")
    compile("io.kotlintest:kotlintest:$KOTLIN_TEST_VER")
    testCompile("io.vertx:vertx-core:$VERTX_VER")
    testCompile("io.vertx:vertx-lang-kotlin:$VERTX_VER")
    testCompile("io.vertx:vertx-web:$VERTX_VER")
}

val compileKotlin by tasks.getting(KotlinCompile::class) {
    kotlinOptions.jvmTarget = "1.8"
}
val compileTestKotlin by tasks.getting(KotlinCompile::class) {
    kotlinOptions.jvmTarget = "1.8"
}
val shadowJar by tasks.getting(ShadowJar::class) {
    baseName = "vertx-kotlintest"
    version = "0.1-SNAPSHOT"
    classifier = ""
}

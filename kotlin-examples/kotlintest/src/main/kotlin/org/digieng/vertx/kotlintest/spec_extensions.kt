package org.digieng.vertx.kotlintest

import io.kotlintest.Spec

fun Spec.runTest(before: () -> Unit, after: () -> Unit, block: () -> Unit) {
    try {
        before()
        block()
    } finally {
        after()
    }
}
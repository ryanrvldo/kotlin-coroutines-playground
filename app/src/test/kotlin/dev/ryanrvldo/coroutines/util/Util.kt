package dev.ryanrvldo.coroutines.util

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

suspend inline fun foo(): Int {
    delay(1000)
    return 10
}

suspend fun bar(): Int {
    delay(1000)
    return 20
}

suspend fun getSum(): Int = coroutineScope {
    val foo = async { foo() }
    val bar = async { bar() }
    foo.await() + bar.await()
}

fun printTotalTime(time: Long) = println("Total time: $time")
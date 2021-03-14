package dev.ryanrvldo.coroutines

import dev.ryanrvldo.coroutines.util.bar
import dev.ryanrvldo.coroutines.util.foo
import dev.ryanrvldo.coroutines.util.printTotalTime
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class AsyncTest {

    @Test
    fun testAsync() = runBlocking {
        val time = measureTimeMillis {
            val foo: Deferred<Int> = GlobalScope.async { foo() }
            val bar: Deferred<Int> = GlobalScope.async { bar() }
            val totalResult: Int = foo.await() + bar.await()
            println("Result: $totalResult")
        }
        printTotalTime(time)
    }

    @Test
    fun testAwaitAll() = runBlocking {
        val time = measureTimeMillis {
            val foo: Deferred<Int> = GlobalScope.async { foo() }
            val foo2: Deferred<Int> = GlobalScope.async { foo() }
            val bar: Deferred<Int> = GlobalScope.async { bar() }
            val bar2: Deferred<Int> = GlobalScope.async { bar() }
            val totalResult: Int = awaitAll(foo, foo2, bar, bar2).sum()
            println("Result: $totalResult")
        }
        printTotalTime(time)
    }

}
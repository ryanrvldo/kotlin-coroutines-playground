package dev.ryanrvldo.coroutines

import kotlinx.coroutines.awaitAll
import org.junit.jupiter.api.Test
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.system.measureTimeMillis

class FutureTest {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(10)

    private fun getFoo(): Int {
        Thread.sleep(1000)
        return 10
    }

    private fun getBar(): Int {
        Thread.sleep(1000)
        return 20
    }

    @Test
    fun testNonParallel() {
        val time = measureTimeMillis {
            val foo = getFoo()
            val bar = getBar()
            val result = foo + bar
            println("Total: $result")
        }.toDouble()
        println("Total time: ${time/1000} seconds.")
    }

    @Test
    fun testFutureGet() {
        val time = measureTimeMillis {
            val foo: Future<Int> = executorService.submit(Callable { getFoo() })
            val bar: Future<Int> = executorService.submit(Callable { getBar() })
            val result = foo.get() + bar.get()
            println("Total: $result")
        }.toDouble()
        println("Total time: ${time/1000} seconds.")
    }
}
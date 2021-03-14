package dev.ryanrvldo.coroutines

import dev.ryanrvldo.coroutines.util.getSum
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class CoroutineScopeTest {

    private val testScope = CoroutineScope(Dispatchers.IO)

    @Test
    fun `test scope`() {
        testScope.launch {
            delay(2000)
            println("Run 1 - ${Thread.currentThread().name}")
        }
        testScope.launch {
            delay(2000)
            println("Run 2 - ${Thread.currentThread().name}")
        }
        runBlocking {
            delay(4500)
            println("Done")
        }
    }

    @Test
    fun `test cancel scope`() {
        testScope.launch {
            delay(2000)
            println("Run 1 - ${Thread.currentThread().name}")
        }
        testScope.launch {
            delay(3000)
            println("Run 2 - ${Thread.currentThread().name}")
        }
        runBlocking {
            delay(2000)
            testScope.cancel()
            delay(1000)
            println("Done")
        }
    }

    @Test
    fun `test coroutine scope function`() = runBlocking {
        val job = testScope.launch {
            val result = getSum()
            println("Result: $result")
        }
        job.join()
    }

    @Test
    fun `test parent child dispatcher`() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        val job = scope.launch {
            println("Parent scope: ${Thread.currentThread().name}")
            coroutineScope {
                launch {
                    println("Child scope: ${Thread.currentThread().name}")
                }
            }
        }
        runBlocking { job.join() }
    }

    @Test
    fun `test cancel parent dispatcher`() {
        val dispatcher = TestCoroutineDispatcher()
        val scope = TestCoroutineScope(dispatcher)

        val job = scope.launch {
            println("Parent scope: ${Thread.currentThread().name}")
            coroutineScope {
                launch {
                    delay(2000)
                    println("Child scope: ${Thread.currentThread().name}")
                }
            }
        }
        runBlockingTest { job.cancelAndJoin() }
    }

    /*
    * Parent will waiting for child process
    * */
    @Test
    fun `test parent child`() = runBlockingTest {
        val job = launch {
            launch {
                delay(2000)
                println("Child 1 done")
            }
            launch {
                delay(4000)
                println("Child 2 done")
            }
            delay(1000)
            println("Parent done")
        }
        job.join()
    }

    @Test
    fun `test cancel child`() = runBlockingTest {
        val job = launch {
            launch {
                delay(2000)
                println("Child 1 done")
            }
            launch {
                delay(4000)
                println("Child 2 done")
            }
            delay(1000)
            println("Parent done")
        }
        // cancel all children process
        job.cancelChildren()
        job.join()
    }

}
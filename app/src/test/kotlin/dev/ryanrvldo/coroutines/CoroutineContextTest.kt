package dev.ryanrvldo.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class CoroutineContextTest {

    @ExperimentalStdlibApi
    @Test
    fun testCoroutineContext() = runBlockingTest {
        val job = launch {
            println(coroutineContext)
            println(coroutineContext[Job])
            println(coroutineContext[CoroutineDispatcher])
        }
        job.join()
    }

    @Test
    fun `test coroutine name`() {
        val scope = TestCoroutineScope(TestCoroutineDispatcher())
        val job = scope.launch(CoroutineName("parent")) {
            println("Parent run in thread ${Thread.currentThread().name}")
            withContext(CoroutineName("child")) {
                println("Child run in thread ${Thread.currentThread().name}")
            }
        }
        runBlockingTest { job.join() }
    }

    @Test
    fun `test coroutine element`() {
        val dispatcher = Executors.newFixedThreadPool(5).asCoroutineDispatcher()
        val scope = CoroutineScope(Dispatchers.IO + CoroutineName("TEST"))
        val job = scope.launch(CoroutineName("parent") + dispatcher) {
            println("Parent run in thread ${Thread.currentThread().name}")
            withContext(CoroutineName("child") + Dispatchers.IO) {
                println("Child run in thread ${Thread.currentThread().name}")
            }
        }
        runBlocking { job.join() }
    }
}
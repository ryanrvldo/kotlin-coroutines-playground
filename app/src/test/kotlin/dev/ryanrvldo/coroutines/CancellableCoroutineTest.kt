package dev.ryanrvldo.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import java.util.*

@ExperimentalCoroutinesApi
class CancellableCoroutineTest {

    @Test
    fun testNotCancellable() = runBlocking {
        val job = GlobalScope.launch {
            println("Start coroutines: ${Date()}")
            Thread.sleep(2000)
            println("End coroutines: ${Date()} ")
        }
        job.cancel()
        delay(3000)
    }

    @Test
    fun testCancellable() = runBlocking {
        val job = GlobalScope.launch {
            ensureActive()
            println("Start coroutines: ${Date()}")

            ensureActive()
            Thread.sleep(2000)

            ensureActive()
            println("End coroutines: ${Date()} ")
        }
        delay(1000)
        job.cancel()
        delay(3000)
    }

    @Test
    fun testCancelFinally() = runBlockingTest {
        val job = launch {
            try {
                println("Start coroutines: ${Date()}")
                delay(2000)
                println("End coroutines: ${Date()} ")
            } catch (e: CancellationException) {
                println(e.message)
            } finally {
                println("Finish")
            }
        }
        job.cancelAndJoin()
    }

    @Test
    fun `test await cancellation`() = runBlockingTest {
        val job = launch {
            try {
                println("Job start")
                awaitCancellation()

                /* unreachable code */
                // println("Something here")
            } finally {
                println("Cancelled job")
            }
        }
        delay(5000)
        job.cancelAndJoin()
    }
}
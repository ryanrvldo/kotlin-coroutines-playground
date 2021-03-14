package dev.ryanrvldo.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import java.util.*

@ExperimentalCoroutinesApi
class JobTest {

    @Test
    fun testLazyJob() = runBlockingTest {
        val job: Job = GlobalScope.launch(coroutineContext, CoroutineStart.LAZY) {
            delay(2000)
            printLog()
        }
        // start the job
        job.start()
        delay(3000)
    }

    @Test
    fun testJoinJob() = runBlockingTest {
        val job: Job = GlobalScope.launch(coroutineContext) {
            delay(2000)
            printLog()
        }
        // wait the job completed
        job.join()
    }

    @Test
    fun testCancelJob() = runBlockingTest {
        val job: Job = GlobalScope.launch(coroutineContext) {
            delay(5000)
            printLog()
        }
        // cancel job
        job.cancel(message = "Canceled job ${job.key}")
    }

    @Test
    fun testJoinAllJob() = runBlockingTest {
        val job1: Job = GlobalScope.launch(coroutineContext) {
            delay(2000)
            println("Done: ${Date()} - ${Thread.currentThread().name}")
        }
        val job2: Job = GlobalScope.launch(coroutineContext) {
            delay(3000)
            println("Done: ${Date()} - ${Thread.currentThread().name}")
        }
        joinAll(job1, job2)
    }

    private fun printLog() {
        println("Done: ${Date()} - ${Thread.currentThread().name}")
    }

}
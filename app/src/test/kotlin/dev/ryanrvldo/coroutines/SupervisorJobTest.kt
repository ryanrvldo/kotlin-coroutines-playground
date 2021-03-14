package dev.ryanrvldo.coroutines

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class SupervisorJobTest {

    @Test
    fun `test one job throw exception`() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + Job())

        val job1 = scope.launch {
            delay(2000)
            println("Job 1 done")
        }

        val job2 = scope.launch {
            delay(1000)
            throw IllegalArgumentException("Job 2 failed")
        }

        runBlocking {
            joinAll(job1, job2)
        }
    }

    @Test
    fun `test supervisor job`() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + SupervisorJob())

        val job1 = scope.launch {
            delay(2000)
            println("Job 1 done")
        }

        val job2 = scope.launch {
            delay(1000)
            throw IllegalArgumentException("Job 2 failed")
        }

        runBlocking {
            joinAll(job1, job2)
        }
    }

    @Test
    fun `test supervisor scope function`() {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + Job())
        runBlocking {
            val job = scope.launch {
                supervisorScope {
                    launch {
                        delay(2000)
                        println("Child 1 done")
                    }
                    launch {
                        delay(1000)
                        throw IllegalArgumentException("Child 2 error")
                    }
                }
            }
            job.join()
        }
    }

}
package dev.ryanrvldo.coroutines

import dev.ryanrvldo.coroutines.util.CoroutineTestExtension
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.RegisterExtension
import java.util.*
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class CoroutineDispatcherTest {

    @JvmField
    @RegisterExtension
    val testExtension = CoroutineTestExtension()

    @Test
    fun testDispatcher() = runBlockingTest {
        println("runBlocking run in ${Thread.currentThread().name}")
        val job1 = launch(Dispatchers.Default) { println("Job 1 run in ${Thread.currentThread().name}") }
        val job2 = launch(Dispatchers.IO) { println("Job 2 run in ${Thread.currentThread().name}") }
        val job3 = launch(TestCoroutineDispatcher()) { println("Job 3 run in ${Thread.currentThread().name}") }
        val job4 = launch(Dispatchers.Unconfined) { println("Job 4 run in ${Thread.currentThread().name}") }
        joinAll(job1, job2, job3, job4)
    }

    @Test
    fun `test dispatcher main without additional library not throw IllegalStateException`() {
        assertDoesNotThrow {
            runBlockingTest {
                val job = launch(Dispatchers.Main) { println("Job run in ${Thread.currentThread().name}") }
                job.join()
            }
        }
    }

    @Test
    fun `test unconfined dispatcher`() = runBlocking {
        println("runBlocking run in ${Thread.currentThread().name}")
        val unconfinedJob = launch(Dispatchers.Unconfined) {
            println("Unconfined: ${Thread.currentThread().name} - ${Date()}")
            delay(1000)
            println("Unconfined: ${Thread.currentThread().name} - ${Date()}")
            delay(1000)
            println("Unconfined: ${Thread.currentThread().name} - ${Date()}")
        }
        val confinedJob = launch {
            println("Confined: ${Thread.currentThread().name} - ${Date()}")
            delay(1000)
            println("Confined: ${Thread.currentThread().name} - ${Date()}")
            delay(1000)
            println("Confined: ${Thread.currentThread().name} - ${Date()}")
        }
        joinAll(unconfinedJob, confinedJob)
    }

    @Test
    fun `test create custom dispatcher`() {
        val serviceDispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val webDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        runBlocking {
            val serviceJob = launch(serviceDispatcher) { println("Service job: ${Thread.currentThread().name}") }
            val webJob = launch(webDispatcher) { println("Web job: ${Thread.currentThread().name}") }
            joinAll(serviceJob, webJob)
        }
    }

    @Test
    fun `test with context`() {
        val clientDispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        runBlocking {
            val job = launch(Dispatchers.IO) {
                println("1 - ${Thread.currentThread().name}")
                withContext(clientDispatcher) {
                    println("2 - ${Thread.currentThread().name}")
                }
                println("3 - ${Thread.currentThread().name}")
                withContext(clientDispatcher) {
                    println("4 - ${Thread.currentThread().name}")
                }
            }
            job.join()
        }
    }

    @Test
    fun `test non cancellable context`() = runBlocking {
        val job = GlobalScope.launch {
            try {
                println("Start job - ${Date()}")
                delay(2000)
                println("Finish job - ${Date()}")
            } finally {
                withContext(NonCancellable) {
                    println(isActive)
                    delay(2000)
                    println("Finally - ${Date()}")
                }
            }
        }
        job.cancelAndJoin()
    }
}
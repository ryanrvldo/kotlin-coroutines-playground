package dev.ryanrvldo.coroutines

import dev.ryanrvldo.coroutines.util.bar
import dev.ryanrvldo.coroutines.util.foo
import dev.ryanrvldo.coroutines.util.printTotalTime
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

class SequentialSuspendFunctionTest {

    @Test
    fun testSequential() = runBlocking {
        val timeMillis = measureTimeMillis {
            foo()
            bar()
        }
        printTotalTime(timeMillis)
    }

    @Test
    fun testSequentialCoroutines() = runBlocking {
        val job = GlobalScope.launch {
            val timeMillis = measureTimeMillis {
                foo()
                bar()
            }
            printTotalTime(timeMillis)
        }
        job.join()
    }

    /*
    * Concurrent two method with delay
    * Can't get return value with launch which is return job
    * */
    @Test
    fun testConcurrent() = runBlocking {
        val time = measureTimeMillis {
            val jobFoo = GlobalScope.launch { foo() }
            val jobBar = GlobalScope.launch { bar() }
            joinAll(jobFoo, jobBar)
        }
        printTotalTime(time)
    }

    private suspend fun runJob(number: Int) {
        println("Start job $number in thread ${Thread.currentThread().name}")
        yield()
        println("End job $number in thread ${Thread.currentThread().name}")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test yield function`() {
        val dispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        runBlockingTest {
            scope.launch { runJob(1) }
            scope.launch { runJob(2) }
            delay(2000)
        }
    }
}
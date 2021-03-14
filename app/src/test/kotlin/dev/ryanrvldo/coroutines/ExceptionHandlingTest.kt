package dev.ryanrvldo.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ExceptionHandlingTest {

    @Test
    fun `test exception launch`() = runBlocking {
        val job = GlobalScope.launch {
            println("Start coroutine")
            throw IllegalArgumentException()
        }
        job.join()
        println("Finish")
    }

    @Test
    fun `test exception async`() = runBlockingTest {
        val deferred = async<Unit> {
            println("Start coroutine")
            throw IllegalArgumentException("throw error")
        }
        try {
            deferred.await()
        } catch (e: IllegalArgumentException) {
            println(e.message)
        } finally {
            println("Finish")
        }
    }

    @Test
    fun `test exception handler`() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            println("Oops error ${throwable.message}")
        }
        val scopeTest = CoroutineScope(Dispatchers.IO + exceptionHandler)
        runBlocking {
            val job = GlobalScope.launch(exceptionHandler) {
                println("Start coroutine 1")
                throw IllegalArgumentException("error 1 in ${Thread.currentThread().name}")
            }
            job.join()
            println("Finish 1")

            val job2 = scopeTest.launch {
                println("Start coroutine 2")
                throw IllegalArgumentException("error 2 in ${Thread.currentThread().name}")
            }
            job2.join()
            println("Finish 2")
        }
    }

}
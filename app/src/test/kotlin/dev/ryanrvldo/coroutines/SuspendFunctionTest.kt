package dev.ryanrvldo.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import java.util.*

@ExperimentalCoroutinesApi
class SuspendFunctionTest {

    private suspend fun helloWorld() {
        println("Hello: ${Date()} - ${Thread.currentThread().name}")
        delay(1000)
        println("World: ${Date()} - ${Thread.currentThread().name}")
    }

    @Test
    fun testSuspendFunction() = runBlockingTest {
        helloWorld()
    }


}
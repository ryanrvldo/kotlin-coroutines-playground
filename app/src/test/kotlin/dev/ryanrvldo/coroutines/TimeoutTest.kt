package dev.ryanrvldo.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import java.util.*

@ExperimentalCoroutinesApi
class TimeoutTest {

    @Test
    fun testTimeout() = runBlockingTest {
        val job = launch {
            withTimeout(5_000) {
                repeat(100) {
                    delay(1000)
                    println("$it ${Date()}")
                }
            }
            println("Finish")
        }
        job.join()
    }

    @Test
    fun testTimeoutOrNull() = runBlockingTest {
        val job = launch {
            withTimeoutOrNull(5_000) {
                repeat(100) {
                    delay(1000)
                    println("$it ${Date()}")
                }
            }
            println("Finish")
        }
        job.join()
    }

}
package dev.ryanrvldo.coroutines

import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ExecutorServiceTest {

    @Test
    fun testSingleThreadPool() {
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        repeat(10) {
            val runnable = Runnable {
                Thread.sleep(1000)
                println("Done $it ${Thread.currentThread().name} ${Date()}")
            }
            executorService.execute(runnable)
        }
        println("Waiting...")
        Thread.sleep(11_000)
        println("Finished")
    }

    @Test
    fun testFixThreadPool() {
        val executorService: ExecutorService = Executors.newFixedThreadPool(3)
        repeat(10) {
            val runnable = Runnable {
                Thread.sleep(1000)
                println("Done $it ${Thread.currentThread().name} ${Date()}")
            }
            executorService.execute(runnable)
        }

        println("Waiting...")
        Thread.sleep(11_000)
        println("Finished")
    }

    @Test
    fun testCachedThreadPool() {
        val executorService: ExecutorService = Executors.newCachedThreadPool()
        repeat(100) {
            val runnable = Runnable {
                Thread.sleep(1000)
                println("Done $it ${Thread.currentThread().name} ${Date()}")
            }
            executorService.execute(runnable)
        }
        println("Waiting...")
        Thread.sleep(1000)
        println("Finished")
    }
}
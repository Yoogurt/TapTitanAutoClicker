@file:Suppress("NOTHING_TO_INLINE")

import kotlinx.coroutines.delay
import java.io.Closeable
import kotlin.random.Random

class TapTitanCommandController : Closeable {
    var randomBound = 10

    private val mRandom = Random(System.currentTimeMillis())
    fun randomRange(): Int = mRandom.nextInt(-randomBound, randomBound)
    private val adbCommand: AdbInputCommand = AdbInputCommand()

    suspend fun enterHeroesList() {
//        adbCommand.tap(270, 1880)
    }

    suspend fun collectFairly() {
        println("collectFairly")
        (100..900 step 100).forEach {
            adbCommand.tap(it + randomRange(), 500 + randomRange())
            delay(300L + randomRange())
        }
        println("clicking confirm fairly video")
        delay(1000L + randomRange())
        adbCommand.tap(800 + randomRange(), 1400 + randomRange())
    }

    suspend fun upgradeHeroes() {
        println("upgradeHeroes")
        adbCommand {
            swipe(500 + randomRange(), 1400 + randomRange(), 500 + randomRange(), 1600 + randomRange(), 1000)

            tap(970 + randomRange(), 1360 + randomRange())
            delay(1000L + randomRange())

            tap(970 + randomRange(), 1530 + randomRange())
            delay(1000L + randomRange())

            tap(970 + randomRange(), 1710 + randomRange())
        }
    }

    override fun close() {
        adbCommand.close()
    }
}
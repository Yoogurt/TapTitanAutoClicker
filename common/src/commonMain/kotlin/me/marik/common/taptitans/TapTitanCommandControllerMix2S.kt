@file:Suppress("NOTHING_TO_INLINE", "NAME_SHADOWING")

import kotlinx.coroutines.*
import me.marik.common.TapTitanViewModel
import me.marik.common.taptitans.Command
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

open class TapTitanCommandControllerMix2S(protected val tapTitanViewModel: TapTitanViewModel) :
    ITapTitanCommandController {
    protected inline operator fun Command.invoke(action: Command.() -> Unit) {
        action()
    }

    protected val command: Command = Command()
    private var mClose = false

    override suspend fun reborn() {
        println("reborn")
        closePanel()

        suspend fun Command.confirmPrestige(offset: Boolean) {
            val prestigeY = if (!offset) 1760 else 1880
            tap(530, prestigeY)
            delay(1000)

            val confirmY = if (!offset) 1500 else 1550
            tap(750, confirmY)
            delay(500)
            tap(750, confirmY)
            delay(500)
        }

        command {
            tap(550, 1300)
            delay(500)
            tap(550, 1300)

            delay(1000)
            tap(100, 2100)
            delay(1000)

            tap(900, 1680)
            delay(500)

            confirmPrestige(false)
            delay(1000)
            confirmPrestige(true)
        }
        delay(15000)
        println("reborn finish")
    }

    override suspend fun upgrade() {
        closePanel()

        command {
            tap(810, 2100)
            delay(1000)

            tap(930, 1700)
            delay(500)
            tap(930, 1750)
            delay(500)
            tap(930, 1800)

            delay(500)
            if (tapTitanViewModel.upgradeSwipe.value) {
                swipe(550, 1800, 550, 1625, 2000)
            }
        }

        println("upgrade finish")
    }

    protected suspend fun reset() {
        command {
            repeat(10) {
                swipe(550, 1800, 550, 100, 2000)
                delay(2500)
            }
        }

        println("upgrade finish")
    }

    protected suspend fun closePanel() {
        command {
            repeat(2) {
                tap(1010, 1179)
                delay(1000)
            }
        }
    }

    protected suspend fun autoUpgrade() {
        command {
            tap(280, 2100)
            delay(1000)
            repeat(3) {
                tap(640, 1307)
                delay(1000)
            }
        }
    }

    protected suspend fun restartPackage() {
        command {
            println("stopping package")
            forceStop()
            delay(5000)
            openApplication()
            upgradeSkill()
            autoUpgrade()
            if (tapTitanViewModel.upgrade.value) {
                swipePrestigePanel()
            }
            tapTitanViewModel.outRestartCount.value++
        }
    }

    protected suspend fun swipePrestigePanel() {
        closePanel()
        command {
            tap(810, 2100)
            delay(1000)

            repeat(tapTitanViewModel.outRestartCount.value % 5) {
                swipe(550, 1800, 550, 200, 2000)
            }
        }
    }

    protected suspend fun
            openApplication() {
        command {
            if (tapTitanViewModel.mainPackage.value) {
                tap(910, 210)
            } else {
                tap(750, 210)
            }
            delay(20000)
        }
    }

    protected suspend fun upgradeSkill() {
        println("run skill")
        command {
            repeat(3) {
                tap(100, 1900)
                delay(500)

                tap(270, 1900)
                delay(500)

                tap(450, 1900)
                delay(500)

                tap(620, 1900)
                delay(500)

                tap(780, 1900)
                delay(500)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    open suspend fun run() {
        var prestigeFirst = tapTitanViewModel.prestigeFirst.value
        suspend fun tryPrestige() {
            val timeCost = measureTime {
                if (tapTitanViewModel.upgrade.value) {
                    upgrade()
                }
                reborn()
                tapTitanViewModel.outPrestigeCount.value++
            }
            println("loop #${tapTitanViewModel.outPrestigeCount.value} cost $timeCost ms")
        }

        if (tapTitanViewModel.restartFirst.value) {
            restartPackage()
        }

        while (!mClose) {
            currentCoroutineContext().ensureActive()

            if ((tapTitanViewModel.outPrestigeCount.value + 1) % (tapTitanViewModel.restartDuration.value.toIntOrNull()
                    ?: 10) == 0
            ) {
                restartPackage()
            }

            if (prestigeFirst) {
                tryPrestige()
            } else {
                prestigeFirst = true
            }
            stepRunDuration((tapTitanViewModel.duration.value.toLongOrNull() ?: 90) * 1000)
        }

        close()
    }

    suspend fun stepRunDuration(duration: Long) {
        var remainDuration = duration
        val threshold = duration / 10

        while (remainDuration > 0) {
            println("remain to sleep $remainDuration")
            remainDuration -= threshold
            delay(threshold)
        }
    }

    suspend fun CoroutineScope.waitFor(printOut: Boolean): Int = command.run { waitFor(printOut) }

    fun close() {
        mClose = true
        command.close()
    }
}
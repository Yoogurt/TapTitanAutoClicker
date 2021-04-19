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
    private var mUpgradeSwipeCount = 0

    override suspend fun reborn() {
        println("reborn")
        closePanel()

        suspend fun Command.confirmPrestige() {
            var prestigeY = 1720
            tap(530, prestigeY)
            delay(1000)

            var confirmY = 1420
            tap(750, confirmY)
            delay(500)

            prestigeY = 1760
            tap(530, prestigeY)
            delay(1000)

            confirmY = 1500
            tap(750, confirmY)
            delay(500)

            prestigeY = 1880
            tap(530, prestigeY)
            delay(1000)

            confirmY = 1550
            tap(750, confirmY)
            delay(500)
        }

        command {

            // any unexpected confirm
            (1300 until 1800 step 100).forEach {
                tap(550, it)
                delay(500)
            }

            delay(1000)
            tap(100, 2100)
            delay(1000)

            tap(900, 1680)
            delay(500)

            confirmPrestige()
            delay(1000)
        }
        delay(15000)
        println("reborn finish")
    }

    override suspend fun upgrade() {
        autoUpgrade()
        closePanel()

        command {
            tap(810, 2100)
            delay(1000)

            repeat(tapTitanViewModel.upgradeSwipeRepeatCount.value.toIntOrNull() ?: 0) {
                tap(930, 1700)
                delay(500)
                mUpgradeSwipeCount++

                if (tapTitanViewModel.upgradeSwipe.value) {
                    swipePrestigePanelOneCell()
                }
            }

            if (mUpgradeSwipeCount > tapTitanViewModel.upgradeSwipeAfterReset.value.toIntOrNull() ?: 50) {
                resetPrestigePanel()
                mUpgradeSwipeCount = 0
            }
        }
    }

    protected suspend fun swipePrestigePanelOneCell() {
        command {
            swipe(550, 1800, 550, 1625, 1000)
            delay(1000)
        }
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
        closePanel()
        command {
            tap(280, 2100)
            delay(1000)
            tap(640, 1307)
            delay(1000)
        }
    }

    protected suspend fun restartPackage() {
        command {
            forceStop()
            if (tapTitanViewModel.swapContextAfterRestart.value){
                tapTitanViewModel.swapPackageContext()
            }
            delay(5000)
            openApplication()
            checkInTournament()
            upgradeSkill()
            autoUpgrade()
            tapTitanViewModel.outRestartCount.value++
        }
    }

    protected suspend fun checkInTournament() {
        if (tapTitanViewModel.inAbyssalTournament.value) {
            command {
                tap(80, 300)
                delay(1000)
                repeat(4) {
                    tap(61, 480)
                    delay(500)
                }
                repeat(2) {
                    tap(840, 1870)
                    delay(1000)
                }
            }

            delay(20000)
        }
    }

    protected suspend fun resetPrestigePanel() {
        closePanel()
        command {
            tap(810, 2100)
            delay(1000)

            repeat(15) {
                swipe(550, 1550, 550, 2000, 1000)
                delay(1300)
            }
        }
    }

    protected suspend fun
            openApplication() {
        command {
            if (tapTitanViewModel.mainPackage) {
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
            repeat(4) {
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

                tap(1000, 1900)
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
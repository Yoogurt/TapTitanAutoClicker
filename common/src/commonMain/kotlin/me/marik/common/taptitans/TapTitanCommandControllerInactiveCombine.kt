package me.marik.common.taptitans

import TapTitanCommandControllerMix2S
import androidx.compose.runtime.AtomicReference
import kotlinx.coroutines.*
import me.marik.common.TapTitanViewModel
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class TapTitanCommandControllerInactiveCombine(
    tapTitanViewModel: TapTitanViewModel
) : TapTitanCommandControllerMix2S(tapTitanViewModel) {

    private val suspendedCont = AtomicReference<Continuation<Boolean>?>(null)
    private val running = AtomicReference(false)

    override suspend fun run() {

        suspend fun runLoopOnce() {
            openApplication()
            if (tapTitanViewModel.upgrade.value) {
                autoUpgrade()
                upgrade(closePanel = false)
            }
            reborn(!tapTitanViewModel.upgrade.value)
            command {
                if (tapTitanViewModel.realStop.value) {
                    stopPackage()
                } else {
                    backgroundPackage()
                }
            }
        }

        command {
            backgroundPackage()
        }

        coroutineScope {

            if (tapTitanViewModel.mainPackageContext.doInactive.value) {
                // main package
                launch {
                    while (true) {
                        println("main package run loop")

                        val duration = switchContextAndRunOnce {
                            println("enter main package run loop")
                            tapTitanViewModel.swapPackageContextTo(true)
                            runLoopOnce()
                            val result = (tapTitanViewModel.duration.value.toLongOrNull() ?: 120) * 1000
                            println("exit main package run loop")
                            result
                        }

                        delay(duration)
                    }
                }
            }

            if (tapTitanViewModel.subPackageContext.doInactive.value) {
                // sub package
                launch {
                    while (true) {
                        println("sub package run loop")
                        val duration = switchContextAndRunOnce {
                            println("enter sub package run loop")
                            tapTitanViewModel.swapPackageContextTo(false)
                            runLoopOnce()
                            val result = (tapTitanViewModel.duration.value.toLongOrNull() ?: 120) * 1000
                            println("exit sub package run loop")
                            result
                        }

                        delay(duration)
                    }
                }
            }
        }
    }

    private suspend fun <R> switchContextAndRunOnce(action: suspend () -> R): R {

        while (!suspendCancellableCoroutine<Boolean> { cont ->
                if (running.compareAndSet(false, true)) {
                    println("run immediate -> $cont")
                    cont.resume(true)
                } else {
                    println("suspend -> $cont")
                    suspendedCont.set(cont)
                }
            });

        val result = action()
        println("action done")

        running.set(false)
        suspendedCont.get()?.also {
            print("resume -> $it")
            suspendedCont.set(null)
            it.resume(false)
        }

        return result
    }
}
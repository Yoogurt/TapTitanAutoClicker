@file:Suppress("NOTHING_TO_INLINE", "BlockingMethodInNonBlockingContext")

package me.marik.common.taptitans

import android.os.Build
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.lang.IllegalArgumentException

private const val WINDOW_ENTER = "\n"

actual class Command {

    private inline fun String.enter(): String = "$this$WINDOW_ENTER"

    private val mProcess: Process = ProcessBuilder().command("su", "shell").start()
    private val output: OutputStreamWriter = mProcess.outputStream.writer()

    private inline fun throwIfProcessDestory() {
        if (Build.VERSION.SDK_INT > 26 && !mProcess.isAlive) {
            throw IllegalArgumentException("process dead")
        }
    }

    actual suspend fun swipe(fromX: Int, fromY: Int, toX: Int, toY: Int, duration: Long) {
        throwIfProcessDestory()
        output.append("input swipe $fromX $fromY $toX $toY $duration".enter())
            .flush()
        delay(duration)
    }

    actual suspend fun forceStop(): Unit {
        throwIfProcessDestory()
        repeat(5) {
            output.append("input keyevent 4".enter()).flush()
            delay(500)
        }
        delay(1000)
    }

    actual suspend fun tap(x: Int, y: Int): Unit {
        throwIfProcessDestory()
        output.append("input tap $x $y".enter()).flush()
    }

    actual suspend fun back(): Unit {
        throwIfProcessDestory()
        output.append("input keyevent 4".enter()).flush()
    }

    actual fun close() {
        println("stopping command")
        output.close()
        mProcess.destroy()
    }


    actual suspend fun CoroutineScope.waitFor(
        printOut: Boolean
    ): Int {
        throwIfProcessDestory()

        if (printOut) {
            println("start reading in")

            launch {
                val reader = mProcess.inputStream.reader()
                reader.forEachLine {
                    println("from command in -> $it")
                }
                println("end reading in")
            }
            launch {
                println("start reading err")
                val reader = mProcess.errorStream.reader()

                reader.forEachLine {
                    println("from command err -> $it")
                }
                println("end reading err")
            }
        }

        return mProcess.waitFor()
    }
}


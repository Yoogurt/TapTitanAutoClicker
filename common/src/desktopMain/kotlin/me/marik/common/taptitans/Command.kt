package me.marik.common.taptitans

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import java.io.OutputStreamWriter

private const val IP_ADDRESS = "192.168.199.178:5555"
private const val WINDOW_ENTER = "\r\n"

actual class Command {

    private inline fun String.enter(): String = "$this $WINDOW_ENTER"

    private val mProcess: Process = ProcessBuilder().command("cmd").start()
    private val output: OutputStreamWriter = mProcess.outputStream.writer()

    init {
        output.append("adb -s c37d89e3 shell".enter()).flush()
    }

    actual suspend fun swipe(fromX: Int, fromY: Int, toX: Int, toY: Int, duration: Long) {
        output.append("input swipe $fromX $fromY $toX $toY $duration".enter())
            .flush()
        delay(duration)
    }

    actual suspend fun forceStop(): Unit {
        repeat(5) {
            output.append("input keyevent 4".enter()).flush()
            delay(500)
        }
        delay(1000)
    }

    actual suspend fun tap(x: Int, y: Int): Unit =
        output.append("input tap $x $y".enter()).flush()

    actual suspend fun back(): Unit = output.append("input keyevent 4".enter()).flush()

    actual fun close() {
        output.close()
    }

    actual suspend fun CoroutineScope.waitFor(printOut: Boolean) = 0
}
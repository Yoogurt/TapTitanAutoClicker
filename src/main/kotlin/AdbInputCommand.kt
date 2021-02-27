@file:Suppress("NOTHING_TO_INLINE", "BlockingMethodInNonBlockingContext")

import kotlinx.coroutines.delay
import java.io.Closeable
import java.io.OutputStreamWriter

private const val WINDOW_ENTER = "\r\n"

class AdbInputCommand : Closeable {

    private inline fun String.enter(): String = "$this $WINDOW_ENTER"

    private val mProcess: Process = ProcessBuilder().command("cmd").start()
    private val output: OutputStreamWriter = mProcess.outputStream.writer()

    init {
        output.append("adb shell".enter()).flush()
    }

    suspend fun swipe(fromX: Int, fromY: Int, toX: Int, toY: Int, duration: Long) {
        output.append("input swipe $fromX $fromY $toX $toY $duration".enter()).flush()
        delay(duration)
    }

    suspend fun tap(x: Int, y: Int): Unit = output.append("input tap $x $y".enter()).flush()

    suspend fun back(): Unit = output.append("input keyevent 4".enter()).flush()

    override fun close() {
        output.close()
        mProcess.destroy()
    }
}
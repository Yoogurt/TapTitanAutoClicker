import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume

class TapTitanClickerController {
    private val _repeatCount = mutableStateOf(0)
    val repeatCount: State<Int> = _repeatCount

    private var mCurrentJob = mutableStateOf<Job?>(null)
    val currentRunningJob: State<Job?> = mCurrentJob

    private fun newCmdProcess() = ProcessBuilder().command("cmd").start()

    suspend fun startAutoCollect(duration: Long = 5000): Boolean {
        suspendCancellableCoroutine<Unit> { cont ->
            val previousJob = mCurrentJob.value

            if (previousJob == null) {
                println("previousJob is empty , resumed !")
                cont.resume(Unit)
                return@suspendCancellableCoroutine
            }

            previousJob.invokeOnCompletion {
                println("previousJob was cancelled , resumed !")
                cont.resume(Unit)
            }

            previousJob.cancel()
        }

        check(mCurrentJob.value == null)

        val currentJob = coroutineContext[Job]
        mCurrentJob.value = currentJob
        currentJob?.invokeOnCompletion {
            println("job $currentJob complete , exception: $it")
            if (currentJob == mCurrentJob.value) {
                mCurrentJob.value = null
            }
        }

        val process = newCmdProcess()

        currentJob?.invokeOnCompletion {
            println("destroying process")
            process.destroy()
        }

        val adbCommand = AdbInputCommand()

        adbCommand.use {
            while (true) {
                // run collect fairly
                adbCommand.swipe(0, 600, 1080, 600, 2000)
                delay(1000)
                adbCommand.tap(689, 1057)

                _repeatCount.value++
                delay(duration)
            }
        }
    }

    fun cancel() {
        mCurrentJob.value?.cancel()
    }
}
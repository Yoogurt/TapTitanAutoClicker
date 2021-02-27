import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.system.measureTimeMillis

class TapTitanClickerController {
    private val _repeatCount = mutableStateOf(0)
    val repeatCount: State<Int> = _repeatCount

    private var mCurrentJob = mutableStateOf<Job?>(null)
    val currentRunningJob: State<Job?> = mCurrentJob

    private fun newCmdProcess() = ProcessBuilder().command("cmd").start()

    suspend fun startCollectFairAndUpgradeHeroes(duration: Long = 10000): Boolean {
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

        process.onExit().handle { _, throwable ->
            throwable?.printStackTrace()
            currentJob?.cancel()
        }

        currentJob?.invokeOnCompletion {
            println("destroying process , $it")
            process.destroyForcibly()
        }

        val tapTitanCommandController = TapTitanCommandController()

        tapTitanCommandController.use {
            tapTitanCommandController.enterHeroesList()

            while (currentJob?.isActive == true && process.isAlive) {
                val timeCost = measureTimeMillis {
                    try {
                        // run collect fairly
                        tapTitanCommandController.collectFairly()
                        delay(1000L + tapTitanCommandController.randomRange())
                        tapTitanCommandController.upgradeHeroes()
                    }catch (e:Throwable){
                        e.printStackTrace()
                        throw e
                    }
                }

                println("loop #${_repeatCount.value} cost $timeCost ms")

                _repeatCount.value++
                delay(duration)
            }

            return true
        }
    }

    fun cancel() {
        mCurrentJob.value?.cancel()
    }
}
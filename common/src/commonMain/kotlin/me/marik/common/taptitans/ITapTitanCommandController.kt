interface ITapTitanCommandController : SuspendRunnable {
    suspend fun reborn(closePanel: Boolean = true)
    suspend fun upgrade(closePanel: Boolean = true)
}

interface SuspendRunnable {
    suspend fun run()
}
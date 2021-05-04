package me.marik.common.taptitans

import kotlinx.coroutines.CoroutineScope

expect class Command() {
    suspend fun swipe(fromX: Int, fromY: Int, toX: Int, toY: Int, duration: Long)
    suspend fun stopPackage()
    suspend fun tap(x: Int, y: Int)
    suspend fun back()
    suspend fun CoroutineScope.waitFor(
        printOut: Boolean
    ): Int
    fun close()
    suspend fun backgroundPackage()
}
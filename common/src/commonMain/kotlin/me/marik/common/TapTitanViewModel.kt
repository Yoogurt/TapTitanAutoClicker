package me.marik.common

import TapTitanCommandControllerMix2S
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow

class TapTitanViewModel {
    val prestigeFirst = mutableStateOf(true)
    val restartFirst = mutableStateOf(false)
    val restartDuration = mutableStateOf("10")
    val duration = mutableStateOf("90")
    val printOut = mutableStateOf(false)
    val upgrade = mutableStateOf(false)
    val upgradeSwipe = mutableStateOf(false)
    val inactive = mutableStateOf(false)
    val mainPackage = mutableStateOf(true)

    val runningCommand = mutableStateOf<Job?>(null, policy = neverEqualPolicy())
    val startCountDown = mutableStateOf(0)
}
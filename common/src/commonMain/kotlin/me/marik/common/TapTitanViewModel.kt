package me.marik.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import kotlinx.coroutines.Job

class TapTitanViewModel {

    val packageContext = mutableStateOf<PackageContext>(MainPackageContext())
    var backPackageContext: PackageContext = SubPackageContext()
        private set

    fun swapPackageContextTo(main: Boolean) {
        val isMain = packageContext.value is MainPackageContext
        if (isMain xor main) {
            swapPackageContext()
        }
    }

    fun swapPackageContext() {
        val tempPackageContext = backPackageContext
        backPackageContext = packageContext.value
        packageContext.value = tempPackageContext
    }

    val prestigeFirst: MutableState<Boolean>
        get() = packageContext.value.prestigeFirst
    val restartFirst: MutableState<Boolean>
        get() = packageContext.value.restartFirst
    val restartDuration: MutableState<String>
        get() = packageContext.value.restartDuration
    val duration: MutableState<String>
        get() = packageContext.value.duration
    val printOut = mutableStateOf(false)
    val upgrade: MutableState<Boolean>
        get() = packageContext.value.upgrade
    val upgradeSwipe: MutableState<Boolean>
        get() = packageContext.value.upgradeSwipe
    val upgradeSwipeRepeatCount: MutableState<String>
        get() = packageContext.value.upgradeSwipeRepeatCount
    val upgradeSwipeAfterReset: MutableState<String>
        get() = packageContext.value.upgradeSwipeAfterReset
    val inactive: MutableState<Boolean>
        get() = packageContext.value.inactive
    val mainPackage: Boolean
        get() = packageContext.value.mainPackage
    val inAbyssalTournament: MutableState<Boolean>
        get() = packageContext.value.inAbyssalTournament
    val swapContextAfterRestart = mutableStateOf(false)

    val outPrestigeCount = mutableStateOf(0)
    val outRestartCount = mutableStateOf(0)

    val runningCommand = mutableStateOf<Job?>(null, policy = neverEqualPolicy())
    val startCountDown = mutableStateOf(0)
}
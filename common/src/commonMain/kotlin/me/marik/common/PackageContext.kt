package me.marik.common

import androidx.compose.runtime.MutableState

interface PackageContext {
    val name: String

    val prestigeFirst: MutableState<Boolean>
    val restartFirst: MutableState<Boolean>
    val restartDuration: MutableState<String>
    val duration: MutableState<String>
    val upgrade: MutableState<Boolean>
    val doInactive: MutableState<Boolean>
    val upgradeSwipe: MutableState<Boolean>
    val upgradeSwipeRepeatCount: MutableState<String>
    val upgradeSwipeAfterReset: MutableState<String>
    val mainPackage: Boolean
    val inAbyssalTournament: MutableState<Boolean>
}
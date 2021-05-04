package me.marik.common

import androidx.compose.runtime.mutableStateOf

class SubPackageContext : PackageContext {
    override val name = "SubPackage"

    override val prestigeFirst = mutableStateOf(true)
    override val restartFirst = mutableStateOf(false)
    override val restartDuration = mutableStateOf("10")
    override val duration = mutableStateOf("180")
    override val upgrade = mutableStateOf(false)
    override val upgradeSwipe = mutableStateOf(false)
    override val upgradeSwipeRepeatCount = mutableStateOf("1")
    override val upgradeSwipeAfterReset = mutableStateOf("50")
    override val mainPackage = false
    override val doInactive = mutableStateOf(true)
    override val inAbyssalTournament = mutableStateOf(false)
}
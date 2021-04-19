package me.marik.common

import androidx.compose.runtime.mutableStateOf

class SubPackageContext : PackageContext {
    override val name = "SubPackage"

    override val prestigeFirst = mutableStateOf(true)
    override val restartFirst = mutableStateOf(false)
    override val restartDuration = mutableStateOf("10")
    override val duration = mutableStateOf("90")
    override val upgrade = mutableStateOf(false)
    override val upgradeSwipe = mutableStateOf(false)
    override val upgradeSwipeRepeatCount = mutableStateOf("5")
    override val upgradeSwipeAfterReset = mutableStateOf("50")
    override val inactive = mutableStateOf(false)
    override val mainPackage = false
    override val inAbyssalTournament = mutableStateOf(false)
}
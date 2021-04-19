package me.marik.common

import androidx.compose.runtime.mutableStateOf

class MainPackageContext : PackageContext {
    override val name = "MainPackage"

    override val prestigeFirst = mutableStateOf(true)
    override val restartFirst = mutableStateOf(false)
    override val restartDuration = mutableStateOf("10")
    override val duration = mutableStateOf("70")
    override val upgrade = mutableStateOf(false)
    override val upgradeSwipe = mutableStateOf(false)
    override val upgradeSwipeRepeatCount = mutableStateOf("5")
    override val upgradeSwipeAfterReset = mutableStateOf("50")
    override val inactive = mutableStateOf(false)
    override val mainPackage = true
    override val inAbyssalTournament = mutableStateOf(false)
}
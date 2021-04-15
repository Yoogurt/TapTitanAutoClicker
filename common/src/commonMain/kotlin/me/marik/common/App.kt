package me.marik.common

import TapTitanCommandControllerMix2S
import TapTitanCommandControllerMix2SInactive
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*

@Composable
fun App(dispatcher: CoroutineDispatcher = Dispatchers.Default) {
    val tapTitanViewModel = remember { TapTitanViewModel() }

    DisposableEffect(Unit) {
        onDispose {
            tapTitanViewModel.runningCommand.value?.cancel()
        }
    }

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                Selection(tapTitanViewModel.prestigeFirst, "Prestige First")
                Selection(tapTitanViewModel.restartFirst, "Restart First")
                Selection(tapTitanViewModel.mainPackage, "MainPackage")
                Selection(tapTitanViewModel.upgrade, "Auto Upgrade")
                Selection(tapTitanViewModel.upgradeSwipe, "Upgrade Swipe")
                Selection(tapTitanViewModel.inactive, "Inactive")
                Selection(tapTitanViewModel.printOut, "Print Out")
                Row(modifier = Modifier.width(200.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Prestige Duration")
                    TextField(tapTitanViewModel.duration.value, onValueChange = {
                        tapTitanViewModel.duration.value = it
                    })
                }
                Row(modifier = Modifier.width(200.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Restart Duration")
                    TextField(
                        tapTitanViewModel.restartDuration.value,
                        onValueChange = {
                            tapTitanViewModel.restartDuration.value = it
                        })
                }

                val job = tapTitanViewModel.runningCommand.value
                val countDown = tapTitanViewModel.startCountDown.value

                Button(enabled = countDown == 0, onClick = {
                    if (job == null) {
                        val tapTitanCommandControllerMix2S =
                            if (tapTitanViewModel.inactive.value) TapTitanCommandControllerMix2SInactive(tapTitanViewModel) else TapTitanCommandControllerMix2S(tapTitanViewModel)
                        val coroutineScope = CoroutineScope(dispatcher)

                        val job = coroutineScope.launch {
                            (5 downTo 0).forEach {
                                tapTitanViewModel.startCountDown.value = it
                                delay(1000)
                            }

                            tapTitanCommandControllerMix2S.run()
                        }
                        if (tapTitanViewModel.printOut.value) {
                            coroutineScope.launch {
                                tapTitanCommandControllerMix2S.apply {
                                    waitFor(true)
                                }
                            }.invokeOnCompletion {
                                job.cancel()
                            }
                        }
                        job.invokeOnCompletion {
                            it?.printStackTrace()
                            tapTitanViewModel.startCountDown.value = 0
                            tapTitanCommandControllerMix2S.close()
                            tapTitanViewModel.runningCommand.value = null
                        }
                        tapTitanViewModel.runningCommand.value = job
                    } else {
                        job.cancel()
                        tapTitanViewModel.runningCommand.value = null
                    }
                }) {
                    Text(if (countDown > 0) "starting at $countDown" else if (job == null) "run" else "stop")
                }
            }
        }
    }
}

@Composable
private fun Selection(value: MutableState<Boolean>, text: String) {
    Row {
        Checkbox(value.value, onCheckedChange = {
            value.value = it
        })

        Text(text)
    }
}
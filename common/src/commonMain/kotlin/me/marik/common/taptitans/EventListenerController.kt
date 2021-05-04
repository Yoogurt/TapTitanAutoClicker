package me.marik.common.taptitans

import me.marik.common.TapTitanViewModel

expect open class EventListenerController {
    companion object {
        val instance: EventListenerController
    }

    fun onStart(tapTitanViewModel: TapTitanViewModel)
    fun onStop(tapTitanViewModel: TapTitanViewModel)
}
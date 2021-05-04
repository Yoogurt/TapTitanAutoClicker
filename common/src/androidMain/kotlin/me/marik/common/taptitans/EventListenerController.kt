package me.marik.common.taptitans

import me.marik.common.TapTitanViewModel
interface EventListener{
    fun onStart(tapTitanViewModel: TapTitanViewModel)
    fun onStop(tapTitanViewModel: TapTitanViewModel)
}

actual open class EventListenerController {
    actual companion object {
        actual val instance = EventListenerController()
    }

    private val mListener = mutableListOf<EventListener>()

    fun registerListener(listener: EventListener) {
        mListener += listener
    }

    fun unRegisterListener(listener: EventListener){
        mListener -= listener
    }

    actual fun onStart(tapTitanViewModel: TapTitanViewModel) {
        mListener.forEach {
            it.onStart(tapTitanViewModel)
        }
    }

    actual fun onStop(tapTitanViewModel: TapTitanViewModel) {
        mListener.forEach {
            it.onStop(tapTitanViewModel)
        }
    }
}
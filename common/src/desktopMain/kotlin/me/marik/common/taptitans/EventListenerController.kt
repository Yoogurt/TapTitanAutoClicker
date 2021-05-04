package me.marik.common.taptitans

import me.marik.common.TapTitanViewModel

actual open class EventListenerController {
    actual companion object {
        actual val instance = EventListenerController()
    }

    actual fun onStart(tapTitanViewModel: TapTitanViewModel) {}
    actual fun onStop(tapTitanViewModel: TapTitanViewModel) {}
}
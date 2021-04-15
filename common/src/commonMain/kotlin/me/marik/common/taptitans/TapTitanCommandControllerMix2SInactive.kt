import me.marik.common.TapTitanViewModel

class TapTitanCommandControllerMix2SInactive(tapTitanViewModel : TapTitanViewModel) : TapTitanCommandControllerMix2S(tapTitanViewModel) {
    override suspend fun run() {
        if (tapTitanViewModel.prestigeFirst.value) {
            reborn()
        }

        while (true) {
            command {
                forceStop()
                stepRunDuration((tapTitanViewModel.duration.value.toLongOrNull() ?: 150) * 2000)
            }
            openApplication()
            reborn()
        }
    }
}
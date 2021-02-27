import androidx.compose.desktop.Window
import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Providers
import androidx.compose.ui.selection.AmbientSelectionRegistrar

fun main() = Window(title = "TapTitanClicker") {
    Providers(AmbientSelectionRegistrar provides null) {
        MaterialTheme {
            Scaffold(topBar = {
                TapTitanAppBar()
            }) {
                TapTitanCenterCommand(it)
            }
        }
    }
}

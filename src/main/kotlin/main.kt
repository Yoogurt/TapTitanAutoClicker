import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold

fun main() = Window(title = "TapTitanClicker") {
    MaterialTheme {
        Scaffold(topBar = {
            TapTitanAppBar()
        }) {
            TapTitanCenterCommand(it)
        }
    }
}
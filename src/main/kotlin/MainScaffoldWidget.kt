import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun TapTitanAppBar(){
    TopAppBar(modifier = Modifier.preferredHeight(42.dp)) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = "Clicker",
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TapTitanCenterCommand(paddingValues: PaddingValues = PaddingValues()){
    val controller = remember { TapTitanClickerController() }

    Column(
        modifier = Modifier.padding(paddingValues).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val coroutineScope = rememberCoroutineScope()

        Text("Repeat: ${controller.repeatCount.value}")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            coroutineScope.launch {
                controller.startCollectFairAndUpgradeHeroes()
            }
        }, enabled = controller.currentRunningJob.value == null) {
            Text("Run Collect")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            coroutineScope.launch {
                controller.cancel()
            }
        }) {
            Text("Cancel")
        }
    }
}
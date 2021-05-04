package me.marik.android

import android.content.Context
import android.content.Intent
import me.marik.common.App
import android.os.Bundle
import android.os.PowerManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.WindowManager
import androidx.compose.ui.platform.setContent
import kotlinx.coroutines.Dispatchers
import me.marik.common.TapTitanViewModel
import me.marik.common.taptitans.EventListener
import me.marik.common.taptitans.EventListenerController
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(Dispatchers.IO)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        startActivity(Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        })
    }
}
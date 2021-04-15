package me.marik.android

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import me.marik.common.App
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import kotlinx.coroutines.Dispatchers

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
}
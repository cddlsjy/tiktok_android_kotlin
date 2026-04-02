package com.oversketch.tiktok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.oversketch.tiktok.ui.TikTokApp
import com.oversketch.tiktok.ui.theme.TikTokTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TikTokTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TikTokApp()
                }
            }
        }
    }
}

package com.ipu.ipuoneapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import com.ipu.ipuoneapp.navigation.AppNavGraph
import com.ipu.ipuoneapp.core.ui.theme.IPUOneAppTheme
import com.ipu.ipuoneapp.core.utils.TokenManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val uri = intent?.data

        uri?.let {
            val token = it.getQueryParameter("token")

            token?.let { t ->
                lifecycleScope.launch {
                    TokenManager(this@MainActivity).saveToken(t)
                }
            }
        }
        setContent {
            IPUOneAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val uri = intent?.data

        uri?.let {
            val token = it.getQueryParameter("token")

            token?.let { t ->
                lifecycleScope.launch {
                    TokenManager(this@MainActivity).saveToken(t)
                }
            }
        }
    }
}
package com.example.firebase_messaging

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    object FCMMessageManager {
        var messageTitle = mutableStateOf("")
        var messageBody = mutableStateOf("")
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) Log.d("PERMISO", "Permiso de notificaciones concedido")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntent(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.extras?.let { extras ->
            val title = extras.getString("title") ?: extras.getString("gcm.notification.title")
            val body = extras.getString("body") ?: extras.getString("gcm.notification.body")

            if (title != null || body != null) {
                FCMMessageManager.messageTitle.value = title ?: ""
                FCMMessageManager.messageBody.value = body ?: ""
            }
        }
    }
}
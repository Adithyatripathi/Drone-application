package com.example.droneapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.droneapplication.domain.models.ConnectionStatus
import com.example.droneapplication.presentation.screens.ConnectionScreen
import com.example.droneapplication.presentation.screens.DashboardScreen
import com.example.droneapplication.presentation.viewmodel.DroneViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // We use MaterialTheme to give our app default styling and colors
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DroneApp()
                }
            }
        }
    }
}

@Composable
fun DroneApp(viewModel: DroneViewModel = viewModel()) {
    // These lines listen to the data streams coming from our ViewModel
    val connectionState by viewModel.connectionState.collectAsState()
    val telemetryState by viewModel.telemetryState.collectAsState()

    // Navigation logic: If connected, show Dashboard. Otherwise, show Connection screen.
    if (connectionState.status == ConnectionStatus.CONNECTED) {
        DashboardScreen(
            telemetry = telemetryState,
            connectionStatus = connectionState.status,
            onDisconnect = { viewModel.disconnect() },
            onArm = { viewModel.arm() },
            onDisarm = { viewModel.disarm() },
            onTakeoff = { viewModel.takeoff() },
            onRtl = { viewModel.rtl() }
        )
    } else {
        ConnectionScreen(
            connectionState = connectionState,
            onConnectClick = { ip, port -> viewModel.connect(ip, port) }
        )
    }
}
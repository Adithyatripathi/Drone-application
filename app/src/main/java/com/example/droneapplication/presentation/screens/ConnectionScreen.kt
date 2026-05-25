package com.example.droneapplication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.droneapplication.domain.models.ConnectionState
import com.example.droneapplication.domain.models.ConnectionStatus

@Composable
fun ConnectionScreen(
    connectionState: ConnectionState,
    onConnectClick: (String, String) -> Unit
) {
    // Default values for easy testing
    var ipAddress by remember { mutableStateOf("127.0.0.1") }
    var port by remember { mutableStateOf("14550") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("MAVLink Connection", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = ipAddress,
            onValueChange = { ipAddress = it },
            label = { Text("IP Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = port,
            onValueChange = { port = it },
            label = { Text("Port") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Show a loading spinner if connecting, otherwise show the button
        if (connectionState.status == ConnectionStatus.CONNECTING) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onConnectClick(ipAddress, port) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Connect")
            }
        }

        if (connectionState.status == ConnectionStatus.ERROR) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = connectionState.errorMessage ?: "Unknown Error", color = Color.Red)
        }
    }
}
package com.example.droneapplication.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.droneapplication.domain.models.ConnectionStatus
import com.example.droneapplication.domain.models.TelemetryState

@Composable
fun DashboardScreen(
    telemetry: TelemetryState,
    connectionStatus: ConnectionStatus,
    onDisconnect: () -> Unit,
    onArm: () -> Unit,
    onDisarm: () -> Unit,
    onTakeoff: () -> Unit,
    onRtl: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Top Bar: Connection Status
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val statusColor = when (connectionStatus) {
                    ConnectionStatus.CONNECTED -> Color.Green
                    ConnectionStatus.DISCONNECTED -> Color.Red
                    else -> Color.Yellow
                }
                Box(
                    modifier = Modifier.size(12.dp).background(statusColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = connectionStatus.name, fontWeight = FontWeight.Bold)
            }
            Button(onClick = onDisconnect) { Text("Disconnect") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Telemetry Data Grid
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                TelemetryRow("Latitude", String.format("%.6f", telemetry.latitude))
                TelemetryRow("Longitude", String.format("%.6f", telemetry.longitude))
                TelemetryRow("Altitude", String.format("%.2f m", telemetry.altitude))
                TelemetryRow(
                    "Battery",
                    "${telemetry.batteryPercentage}%",
                    if (telemetry.batteryPercentage < 20) Color(0xFFFFA500) else Color.Unspecified
                )
                TelemetryRow("Flight Mode", telemetry.flightMode)
                TelemetryRow(
                    "Status",
                    if (telemetry.isArmed) "ARMED" else "DISARMED",
                    if (telemetry.isArmed) Color.Red else Color.Green
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Action Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = onArm, enabled = !telemetry.isArmed) { Text("Arm") }
            Button(onClick = onDisarm, enabled = telemetry.isArmed) { Text("Disarm") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = onTakeoff, enabled = telemetry.isArmed) { Text("Takeoff") }
            Button(onClick = onRtl, enabled = telemetry.isArmed) { Text("RTL") }
        }
    }
}

// A small helper component to make our rows look neat
@Composable
fun TelemetryRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Text(text = value, color = valueColor, fontWeight = FontWeight.Bold)
    }
}
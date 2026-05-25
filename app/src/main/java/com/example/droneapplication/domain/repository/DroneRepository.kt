package com.example.droneapplication.domain.repository

import com.example.droneapplication.domain.models.ConnectionState
import com.example.droneapplication.domain.models.TelemetryState
import kotlinx.coroutines.flow.StateFlow

interface DroneRepository {
    // These streams will continuously emit updates to our UI
    val connectionState: StateFlow<ConnectionState>
    val telemetryState: StateFlow<TelemetryState>

    // Connection methods
    suspend fun connect(ip: String, port: Int)
    fun disconnect()

    // Drone Action methods
    fun armDrone()
    fun disarmDrone()
    fun takeoff()
    fun returnToLaunch()
}
package com.example.droneapplication.data.repository

import com.example.droneapplication.domain.models.ConnectionState
import com.example.droneapplication.domain.models.ConnectionStatus
import com.example.droneapplication.domain.models.TelemetryState
import com.example.droneapplication.domain.repository.DroneRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class SimulatedDroneRepositoryImpl : DroneRepository {

    // 1. Setup the streams for Connection and Telemetry
    private val _connectionState = MutableStateFlow(ConnectionState())
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val _telemetryState = MutableStateFlow(TelemetryState())
    override val telemetryState: StateFlow<TelemetryState> = _telemetryState.asStateFlow()

    // 2. Setup coroutines to run our background simulation
    private var telemetryJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // 3. Implement the interface methods
    override suspend fun connect(ip: String, port: Int) {
        _connectionState.value = ConnectionState(ConnectionStatus.CONNECTING)

        // Simulate the time it takes to connect over a network
        delay(1500)

        if (ip.isBlank() || port <= 0) {
            _connectionState.value = ConnectionState(ConnectionStatus.ERROR, "Invalid IP or Port")
            return
        }

        _connectionState.value = ConnectionState(ConnectionStatus.CONNECTED)
        startTelemetrySimulation()
    }

    override fun disconnect() {
        telemetryJob?.cancel()
        _connectionState.value = ConnectionState(ConnectionStatus.DISCONNECTED)
        // Reset telemetry when disconnected
        _telemetryState.value = TelemetryState()
    }

    private fun startTelemetrySimulation() {
        telemetryJob?.cancel()
        telemetryJob = scope.launch {
            while (isActive) {
                _telemetryState.update { current ->
                    // Make the numbers move slightly to look like real flight data
                    current.copy(
                        latitude = current.latitude + Random.nextDouble(-0.0001, 0.0001),
                        longitude = current.longitude + Random.nextDouble(-0.0001, 0.0001),
                        altitude = if (current.isArmed) current.altitude + Random.nextDouble(-0.5, 1.0) else 0.0,
                        batteryPercentage = (current.batteryPercentage - if (current.isArmed) 1 else 0).coerceAtLeast(0)
                    )
                }
                delay(1000) // Update exactly once per second (1Hz)
            }
        }
    }

    // Drone Actions (Changing state updates the simulation!)
    override fun armDrone() { _telemetryState.update { it.copy(isArmed = true) } }

    override fun disarmDrone() { _telemetryState.update { it.copy(isArmed = false, altitude = 0.0) } }

    override fun takeoff() {
        if (_telemetryState.value.isArmed) {
            _telemetryState.update { it.copy(flightMode = "TAKEOFF") }
        }
    }

    override fun returnToLaunch() {
        _telemetryState.update { it.copy(flightMode = "RTL") }
    }
}
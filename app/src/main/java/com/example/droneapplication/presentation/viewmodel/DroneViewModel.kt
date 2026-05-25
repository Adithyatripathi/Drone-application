package com.example.droneapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.droneapplication.data.repository.SimulatedDroneRepositoryImpl
import com.example.droneapplication.domain.repository.DroneRepository
import kotlinx.coroutines.launch

class DroneViewModel(
    // We are manually injecting our simulated repository here for simplicity
    private val repository: DroneRepository = SimulatedDroneRepositoryImpl()
) : ViewModel() {

    // The UI will listen to these streams
    val connectionState = repository.connectionState
    val telemetryState = repository.telemetryState

    // These functions can be triggered by UI buttons
    fun connect(ip: String, port: String) {
        viewModelScope.launch {
            val portInt = port.toIntOrNull() ?: 0
            repository.connect(ip, portInt)
        }
    }

    fun disconnect() = repository.disconnect()
    fun arm() = repository.armDrone()
    fun disarm() = repository.disarmDrone()
    fun takeoff() = repository.takeoff()
    fun rtl() = repository.returnToLaunch()
}
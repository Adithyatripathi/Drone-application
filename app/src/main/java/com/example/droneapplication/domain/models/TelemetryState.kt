package com.example.droneapplication.domain.models

data class TelemetryState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double = 0.0,
    val batteryPercentage: Int = 100,
    val flightMode: String = "STABILIZE",
    val isArmed: Boolean = false
)
package com.rentals.homigo.data

data class MaintenanceRequest(
    val requestId: String = "",
    val tenantName: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
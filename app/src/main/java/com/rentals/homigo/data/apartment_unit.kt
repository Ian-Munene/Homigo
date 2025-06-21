package com.rentals.homigo.data

data class ApartmentUnit(
    val unitId: String = "",
    val unitNumber: String = "",
    val unitType: String = "",
    val rentAmount: Number,
    val tenantName: String? = null
)
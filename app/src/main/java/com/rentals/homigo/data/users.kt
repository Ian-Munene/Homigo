package com.rentals.homigo.data

data class User(
    val fullName: String,
    val unitNumber: String,
    val phoneNumber: String,
    val moveInDate: Long,
    val uid: String,
    var email: String,
    var password: String
)
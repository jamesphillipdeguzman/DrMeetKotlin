package com.example

data class Appointment(
    val id: Int,
    var patientId: Int,
    var doctorId: Int,
    var date: String,
    var reason: String,
)

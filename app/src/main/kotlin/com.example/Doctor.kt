package com.example

data class Doctor(
    val id: Int,
    var firstName: String?, // allow missing firstName for doctor
    var lastName: String,
    var specialty: String,
)

package ru.fav.petcare.domain.model

data class AppointmentModel (
    val id: Long = 0,
    val petName: String = "",
    val groomerName: String = "",
    val serviceName: String = "",
    val price: Int = 0,
    val date: String = "",
    val upcoming: Boolean = false
)
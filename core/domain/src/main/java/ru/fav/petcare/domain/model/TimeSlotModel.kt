package ru.fav.petcare.domain.model

data class TimeSlotModel(
    val id: Long = 0,
    val groomerName: String = "",
    val date: String = "",
    val time: String = "",
)
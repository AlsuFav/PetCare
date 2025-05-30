package ru.fav.petcare.domain.model

data class ServiceModel(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val price: Int = 0
)
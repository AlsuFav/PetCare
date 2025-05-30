package ru.fav.petcare.domain.model

data class PetModel(
    val id: Long = 0,
    val name: String = "",
    val species: String = "",
    val breed: String = "",
    val birthDate: String = "",
    val imagePath: String = "",
)
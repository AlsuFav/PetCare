package ru.fav.petcare.domain.repository

import ru.fav.petcare.domain.model.PetModel

interface PetRepository {
    suspend fun getAllPetsData() : List<PetModel>
    suspend fun getPetData(id: Long) : PetModel
    suspend fun createPet(
        name: String,
        species: String,
        breed: String?,
        birthDate: String
    )
    suspend fun updatePet(
        id: Long,
        name: String,
        birthDate: String
    )
    suspend fun deletePet(id: Long)
}
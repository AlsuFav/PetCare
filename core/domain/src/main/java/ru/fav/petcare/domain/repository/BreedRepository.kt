package ru.fav.petcare.domain.repository

interface BreedRepository {
    suspend fun getAllBreedsContainingQuery(query: String) : List<String>
}
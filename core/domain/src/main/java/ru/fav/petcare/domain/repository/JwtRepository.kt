package ru.fav.petcare.domain.repository

interface JwtRepository {
    suspend fun saveJwt(token: String)
    suspend fun getJwt(): String?
    suspend fun clearJwt()
}

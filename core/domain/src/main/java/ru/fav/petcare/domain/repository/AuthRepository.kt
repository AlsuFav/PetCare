package ru.fav.petcare.domain.repository

import ru.fav.petcare.domain.model.JwtModel

interface AuthRepository {
    suspend fun login(phone: String, password: String): JwtModel

    suspend fun register(
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): JwtModel
}

package ru.fav.petcare.domain.repository

import ru.fav.petcare.domain.models.JwtModel

interface AuthRepository {
    suspend fun login(phone: String, password: String): JwtModel
}

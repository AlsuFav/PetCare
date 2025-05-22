package ru.fav.petcare.domain.repository

import ru.fav.petcare.domain.model.ClientModel
import ru.fav.petcare.domain.model.JwtModel

interface ClientRepository {
    suspend fun getClientData(): ClientModel

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    )

    suspend fun updateClientData(
        firstName: String,
        lastName: String,
        phone: String
    )

    suspend fun deleteClient()
}
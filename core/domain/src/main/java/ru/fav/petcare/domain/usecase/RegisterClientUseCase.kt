package ru.fav.petcare.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.model.JwtModel
import ru.fav.petcare.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterClientUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): JwtModel {
        return withContext(dispatcher) {
            authRepository.register(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                password = password,
                confirmPassword = confirmPassword
            )
        }
    }
}

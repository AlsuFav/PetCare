package ru.fav.petcare.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.models.JwtModel
import ru.fav.petcare.domain.repository.AuthRepository
import javax.inject.Inject

class LoginClientUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(phone: String, password: String): JwtModel {
        return withContext(dispatcher) {
            authRepository.login(phone, password)
        }
    }
}
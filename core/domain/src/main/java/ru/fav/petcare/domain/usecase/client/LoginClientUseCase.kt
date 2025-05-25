package ru.fav.petcare.domain.usecase.client

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.model.JwtModel
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
package ru.fav.petcare.domain.usecase.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.provider.JwtProvider
import ru.fav.petcare.domain.repository.AuthRepository
import ru.fav.petcare.domain.repository.JwtRepository
import javax.inject.Inject

class LoginClientUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val jwtRepository: JwtRepository,
    private val jwtProvider: JwtProvider,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(phone: String, password: String) {
        return withContext(dispatcher) {
            val jwt = authRepository.login(phone, password)
            jwtRepository.saveJwt(jwt.token)
            jwtProvider.setJwt(jwt.token)
        }
    }
}
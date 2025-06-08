package ru.fav.petcare.domain.usecase.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.provider.JwtProvider
import ru.fav.petcare.domain.repository.AuthRepository
import ru.fav.petcare.domain.repository.JwtRepository
import javax.inject.Inject

class RegisterClientUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val jwtRepository: JwtRepository,
    private val jwtProvider: JwtProvider,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {
        return withContext(dispatcher) {
            val jwt = authRepository.register(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                password = password,
                confirmPassword = confirmPassword
            )
            jwtRepository.saveJwt(jwt.token)
            jwtProvider.setJwt(jwt.token)
        }
    }
}
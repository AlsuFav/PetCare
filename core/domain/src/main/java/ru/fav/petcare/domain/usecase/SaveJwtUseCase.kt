package ru.fav.petcare.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.model.JwtModel
import ru.fav.petcare.domain.provider.JwtProvider
import ru.fav.petcare.domain.repository.JwtRepository
import javax.inject.Inject

class SaveJwtUseCase @Inject constructor(
    private val repository: JwtRepository,
    private val provider: JwtProvider,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(jwt: JwtModel) = withContext(dispatcher) {
        repository.saveJwt(jwt.token)
        provider.setJwt(jwt.token)
    }
}

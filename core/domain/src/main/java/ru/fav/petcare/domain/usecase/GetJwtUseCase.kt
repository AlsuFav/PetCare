package ru.fav.petcare.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.fav.petcare.domain.di.qualifier.IoDispatchers
import ru.fav.petcare.domain.exception.NoJwtException
import ru.fav.petcare.domain.provider.JwtProvider
import ru.fav.petcare.domain.repository.JwtRepository
import javax.inject.Inject

class GetJwtUseCase @Inject constructor(
    private val repository: JwtRepository,
    private val provider: JwtProvider,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() {
        return withContext(dispatcher) {
            val jwt = repository.getJwt()
            if (jwt == null) {
                throw NoJwtException(null)
            } else {
                provider.setJwt(jwt)
            }
        }
    }
}
